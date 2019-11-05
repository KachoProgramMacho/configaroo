package amos.group3.gitmodconfig_backend.services;

import amos.group3.gitmodconfig_backend.models.BranchModel;
import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubAPIService {

    private final String baseURL_Github = "https://api.github.com";

    @Autowired
    private RepositoryParser repositoryParser;

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

    public BranchModel[] getBranchesOfRepository(int id){
        RepositoryModel selectedRepository = repositoryParser.getRepositoryById(id);
        final String branchesOfRepositoryURL = baseURL_Github + "/repos/" + selectedRepository.getOwner() + "/" + selectedRepository.getRepo() + "/branches";
        return restTemplate.getForObject(branchesOfRepositoryURL, BranchModel[].class);
    }

}
