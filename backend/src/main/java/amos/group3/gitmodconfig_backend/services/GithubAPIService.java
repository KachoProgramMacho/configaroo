package amos.group3.gitmodconfig_backend.services;

import amos.group3.gitmodconfig_backend.models.BranchModel;
import amos.group3.gitmodconfig_backend.models.CommitModel;
import amos.group3.gitmodconfig_backend.models.ConfigurationRepositoryModel;
import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GithubAPIService {

    @Autowired
    private RepositoryParser repositoryParser;

    @Value("${GITHUB_PERSONAL_TOKEN}")
    private String GITHUB_PERSONAL_TOKEN;

    private final String baseURL_Github = "https://api.github.com";
    private final String createRepositoryURL = "https://api.github.com/user/repos";

    private RestTemplate restTemplate;

    public GithubAPIService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    public List getRepositoryMetadata(){
        ArrayList<RepositoryModel> repositories = repositoryParser.getRepositories();
        List<Object> result = new ArrayList<Object>();
        for(RepositoryModel repo : repositories){
            String url = baseURL_Github + "/repos/" + repo.getOwner() + "/" + repo.getRepo();
            Object response = this.restTemplate.getForObject(url, Object.class);
            result.add(response);
        }
        return result;

    }

    public BranchModel[] getBranchesOfRepository(int repositoryId){
        RepositoryModel selectedRepository = repositoryParser.getRepositoryById(repositoryId);
        final String branchesOfRepositoryURL = baseURL_Github + "/repos/" + selectedRepository.getOwner() + "/" + selectedRepository.getRepo() + "/branches";
        return restTemplate.getForObject(branchesOfRepositoryURL, BranchModel[].class);
    }

    public CommitModel[] getCommitsOfBranchOfRepository(int repositoryId, String branchName){
        RepositoryModel selectedRepository = repositoryParser.getRepositoryById(repositoryId);
        final String commitsOfSelectedBranchURL = baseURL_Github + "/repos/" + selectedRepository.getOwner()
                + "/" + selectedRepository.getRepo() + "/commits?sha="+branchName;

        return this.restTemplate.getForObject(commitsOfSelectedBranchURL,CommitModel[].class);
    }

    public String createRepository(ConfigurationRepositoryModel configurationRepositoryModel){

        configurationRepositoryModel.setAuto_init(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(GITHUB_PERSONAL_TOKEN);

        HttpEntity<ConfigurationRepositoryModel> createNewRepositoryPostRequest = new HttpEntity<>(configurationRepositoryModel, headers);

        return restTemplate.postForObject(createRepositoryURL,createNewRepositoryPostRequest,String.class);
    }
}
