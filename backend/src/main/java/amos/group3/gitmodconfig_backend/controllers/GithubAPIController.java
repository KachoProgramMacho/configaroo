package amos.group3.gitmodconfig_backend.controllers;


import amos.group3.gitmodconfig_backend.models.BranchModel;
import amos.group3.gitmodconfig_backend.models.CommitModel;
import amos.group3.gitmodconfig_backend.models.ConfigurationRepositoryModel;
import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import amos.group3.gitmodconfig_backend.services.GithubAPIService;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class GithubAPIController {

    @Autowired
    private RepositoryParser repositoryParser;

    @Autowired
    private GithubAPIService githubAPIService;

    @GetMapping("/api/repository")
    public ArrayList<RepositoryModel> getRepositories(){
        return repositoryParser.getRepositories();
    }

    @GetMapping("/api/configuration-repository")
    public ArrayList<RepositoryModel> getNotFinalizedConfigurationRepositories(){
        return repositoryParser.getNotFinalizedConfigurationRepositories();
    }

    @DeleteMapping("/api/configuration-repository/{repositoryId}")
    public ResponseEntity<RepositoryModel> deleteConfigurationRepository(@PathVariable int repositoryId){
        RepositoryModel repoToDelete = repositoryParser.deleteRepository(repositoryId);
        if(repoToDelete != null){
            githubAPIService.deleteRepository(repoToDelete);
            return new ResponseEntity<RepositoryModel>(repoToDelete, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/repository/{repositoryId}/branches")
    public BranchModel[] getBranchesOfRepository(@PathVariable int repositoryId){
            return githubAPIService.getBranchesOfRepository(repositoryId);
    }

    @GetMapping("/api/repository/{repositoryId}/branches/{branchName}/commits")
    public CommitModel[] getBranchesOfRepository(@PathVariable int repositoryId, @PathVariable String branchName){
        return githubAPIService.getCommitsOfBranchOfRepository(repositoryId,branchName);
    }

    @PostMapping("/api/repository")
    public ConfigurationRepositoryModel createRepository(@RequestBody ConfigurationRepositoryModel configurationRepositoryModel){

        githubAPIService.createRepository(configurationRepositoryModel);
        //TODO: CREATE method for adding submodules to the repository
        githubAPIService.addSubmodulesToRepository(configurationRepositoryModel);

        repositoryParser.saveNewConfiguration(configurationRepositoryModel);

        return configurationRepositoryModel;
    }

}
