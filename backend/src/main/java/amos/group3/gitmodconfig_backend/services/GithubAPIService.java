package amos.group3.gitmodconfig_backend.services;

import amos.group3.gitmodconfig_backend.models.*;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GithubAPIService {

    @Autowired
    private RepositoryParser repositoryParser;

    @Value("${GITHUB_PERSONAL_TOKEN}")
    private String GITHUB_PERSONAL_TOKEN;

    @Value("${GITHUB_ACCOUNT_OWNER}")
    private String GITHUB_ACCOUNT_OWNER;

    private final String baseURL_Github = "https://api.github.com";
    private final String createRepositoryURL = "https://api.github.com/user/repos";



    private RestTemplate restTemplate;

    public GithubAPIService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
        HttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(4400);
        requestFactory.setReadTimeout(5456);

        restTemplate.setRequestFactory(requestFactory);
    }

    public List getRepositoryMetadata(){
        ArrayList<RepositoryModel> repositories = repositoryParser.getRepositories();
        List<Object> result = new ArrayList<Object>();
        for(RepositoryModel repo : repositories){
            String url = baseURL_Github + "/repos/" + repo.getOwner() + "/" + repo.getName();
            Object response = this.restTemplate.getForObject(url, Object.class);
            result.add(response);
        }
        return result;

    }

    public ResponseEntity<BranchModel[]> getBranchesOfRepository(String repositoryId){
        RepositoryModel selectedRepository = repositoryParser.getRepositoryById(repositoryId);
        final String branchesOfRepositoryURL = baseURL_Github + "/repos/" + selectedRepository.getOwner() + "/" + selectedRepository.getName() + "/branches";
        return restTemplate.exchange(branchesOfRepositoryURL, HttpMethod.GET, null, BranchModel[].class);
    }

    public ResponseEntity<CommitModel[]> getCommitsOfBranchOfRepository(String repositoryId, String branchName){
        RepositoryModel selectedRepository = repositoryParser.getRepositoryById(repositoryId);
        final String commitsOfSelectedBranchURL = baseURL_Github + "/repos/" + selectedRepository.getOwner()
                + "/" + selectedRepository.getName() + "/commits?sha="+branchName;

        return this.restTemplate.exchange(commitsOfSelectedBranchURL,HttpMethod.GET,null, CommitModel[].class);
    }

    public ResponseEntity<String> createRepository(CreateRepositoryModel createRepositoryModel) throws HttpClientErrorException {

        createRepositoryModel.setAuto_init(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(GITHUB_PERSONAL_TOKEN);

        HttpEntity<CreateRepositoryModel> createNewRepositoryPostRequest = new HttpEntity<>(createRepositoryModel, headers);

        return restTemplate.exchange(createRepositoryURL, HttpMethod.POST,createNewRepositoryPostRequest,String.class);
    }

    public void addSubmodulesToRepository(CreateRepositoryModel createRepositoryModel){

        final String commitsOfSelectedBranchURL = baseURL_Github + "/repos/" + GITHUB_ACCOUNT_OWNER
                + "/" + createRepositoryModel.getName() + "/commits?sha=master";



        //1.) Create a git tree
        CommitModel treeCommit = createGitTree(createRepositoryModel);

        //Get last commit of master branch
        CommitModel baseSHA = this.restTemplate.getForObject(commitsOfSelectedBranchURL,CommitModel[].class)[0];

        //2.)
        CommitModel commit = commitGitTree(baseSHA.getSha(),  treeCommit.getSha(), createRepositoryModel);
        //3.)
        pushGitTree(commit.getSha(), createRepositoryModel);

    }

    public CommitModel createGitTree(CreateRepositoryModel createRepositoryModel){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(GITHUB_PERSONAL_TOKEN);


        //Init JSON variables
        JSONObject payload =  new JSONObject();
        JSONArray treeContent = new JSONArray();

        //Init tree root
        JSONObject treeRoot = new JSONObject();
        treeRoot.put("path",".gitmodules");
        treeRoot.put("mode","100644");
        treeRoot.put("type","blob");
        treeRoot.put("content","");

        treeContent.put(treeRoot);

        SubmoduleModel[] submoduleModels = createRepositoryModel.getSubmodules();

        for(SubmoduleModel s:submoduleModels){
            //s.getRepositoryName returns ID and NOT a Name even though the method name suggests otherwise
            RepositoryModel currentRepository = repositoryParser.getRepositoryById(repositoryParser.getIdByRepositoryName(s.getRepositoryName()));

            // Add submodule to tree root
            String contentString = "[submodule \""+currentRepository.getName()+"\"]\n\tpath = " + currentRepository.getName()+"\n\turl = "+currentRepository.getUrl()+"\n";
            treeRoot.put("content", treeRoot.get("content")+contentString);
            System.out.println("Content string: "  + contentString);

            //Init new tree item for current repository
            JSONObject treeItem = new JSONObject();
            treeItem.put("path",currentRepository.getName());
            treeItem.put("mode","160000");
            treeItem.put("type","commit");
            treeItem.put("sha",s.getCommitSHA());

            //Push the new repo object to the tree array
            treeContent.put(treeItem);
        }


        // Add properties to the tree
        payload.put("tree",treeContent);

        HttpEntity<String> createNewGithubTreePostRequest = new HttpEntity<>(payload.toString(), headers);

        return restTemplate.postForObject(baseURL_Github+"/repos/" + GITHUB_ACCOUNT_OWNER +"/" + createRepositoryModel.getName()+"/git/trees",createNewGithubTreePostRequest,CommitModel.class);
    }

    public CommitModel commitGitTree(String baseSHA, String treeSHA, CreateRepositoryModel createRepositoryModel){
        final String url = baseURL_Github + "/repos/" +GITHUB_ACCOUNT_OWNER+"/" + createRepositoryModel.getName() +"/git/commits";
        JSONObject requestBody = new JSONObject();
        requestBody.put("message", "add submodules");
        requestBody.put("tree", treeSHA);
        requestBody.put("parent", new JSONArray().put(baseSHA));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(GITHUB_PERSONAL_TOKEN);

        HttpEntity<String> commitTreeRequest = new HttpEntity<>(requestBody.toString(), headers);
        //return SHA of new tree commit
        return restTemplate.postForObject(url,commitTreeRequest,CommitModel.class);
    }

    public ResponseEntity<String> pushGitTree(String treeCommitSHA, CreateRepositoryModel createRepositoryModel){
        final String url = baseURL_Github + "/repos/" +GITHUB_ACCOUNT_OWNER+"/" + createRepositoryModel.getName() +"/git/refs/heads/master";
        JSONObject requestBody = new JSONObject();
        requestBody.put("sha", treeCommitSHA);
        requestBody.put("force", true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(GITHUB_PERSONAL_TOKEN);


        HttpEntity<String> commitTreeRequest = new HttpEntity<>(requestBody.toString(), headers);
        return restTemplate.exchange(url, HttpMethod.PATCH, commitTreeRequest, String.class);

    }

    public void deleteRepository(RepositoryModel repoToDelete) throws RestClientException {
        final String deleteUrl = baseURL_Github + "/repos/" +GITHUB_ACCOUNT_OWNER+"/" + repoToDelete.getName();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(GITHUB_PERSONAL_TOKEN);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity deleteRequest = new HttpEntity(headers);

        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, deleteRequest, String.class);
    }
}
