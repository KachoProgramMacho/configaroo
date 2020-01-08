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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

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
/*
    @GetMapping("/api/configuration-repository")
    public ArrayList<RepositoryModel> getNotFinalizedConfigurationRepositories(){
        return repositoryParser.getNotFinalizedConfigurationRepositories();
    }*/

    @PutMapping("/api/repository/{repositoryId}/finalize")
    public ResponseEntity<RepositoryModel> editRepository(@PathVariable int repositoryId){
        try{
            RepositoryModel finalized = repositoryParser.finalizeRepository(repositoryId);
            if(finalized != null){
                return new ResponseEntity<>(finalized, OK);
            }else {
                return new ResponseEntity<>(NOT_FOUND);
            }
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/repository/{repositoryId}")
    public ResponseEntity<RepositoryModel> deleteConfigurationRepository(@PathVariable int repositoryId){
        RepositoryModel repoToDelete;
        try{
            repoToDelete = repositoryParser.deleteRepository(repositoryId);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(repoToDelete != null){
            githubAPIService.deleteRepository(repoToDelete);
            return new ResponseEntity<RepositoryModel>(repoToDelete, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/repository/{repositoryId}/branches")
    public ResponseEntity<BranchModel[]> getBranchesOfRepository(@PathVariable int repositoryId){
            ResponseEntity<BranchModel[]> responseEntity = githubAPIService.getBranchesOfRepository(repositoryId);
            if(responseEntity.getStatusCode()!=OK){
                return new ResponseEntity<>(BAD_REQUEST);
            }else {
                return new ResponseEntity<>(responseEntity.getBody(), OK);
            }
    }

    @GetMapping("/api/repository/{repositoryId}/branches/{branchName}/commits")
    public ResponseEntity<CommitModel[]> getBranchesOfRepository(@PathVariable int repositoryId, @PathVariable String branchName){
        ResponseEntity<CommitModel[]> responseEntity = githubAPIService.getCommitsOfBranchOfRepository(repositoryId,branchName);
        if(responseEntity.getStatusCode()!=OK){
            return new ResponseEntity<>(BAD_REQUEST);
        }else {
            return new ResponseEntity<>(responseEntity.getBody(), OK);
        }
    }

    @PostMapping("/api/repository")
    public ResponseEntity<ConfigurationRepositoryModel> createRepository(@RequestBody ConfigurationRepositoryModel configurationRepositoryModel){

        try {
            ResponseEntity createRepoResponse = githubAPIService.createRepository(configurationRepositoryModel);
        }catch (HttpClientErrorException e){

            if(e.getStatusCode().equals(UNAUTHORIZED)){
                return new ResponseEntity<>(UNAUTHORIZED);
            }else if (e.getStatusCode().equals(UNPROCESSABLE_ENTITY)){
                return new ResponseEntity<>(UNPROCESSABLE_ENTITY);
            }
        }
        githubAPIService.addSubmodulesToRepository(configurationRepositoryModel);

        repositoryParser.saveNewConfiguration(configurationRepositoryModel);

        return new ResponseEntity<ConfigurationRepositoryModel>(configurationRepositoryModel, OK);
    }

}
