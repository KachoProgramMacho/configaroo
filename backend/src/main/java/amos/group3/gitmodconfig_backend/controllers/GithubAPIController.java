package amos.group3.gitmodconfig_backend.controllers;


import amos.group3.gitmodconfig_backend.models.*;
import amos.group3.gitmodconfig_backend.services.GithubAPIService;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

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

    @GetMapping("/api/repository/{repositoryId}/submodules")
    public ResponseEntity<SubmoduleModel[]> getRepositorySubmodules(@PathVariable String repositoryId){
        RepositoryModel currentRepo = repositoryParser.getRepositoryById(repositoryId);
        SubmoduleModel[] allSubmodulesOfCurrentRepo = new SubmoduleModel[currentRepo.getSubmodules().length];
        for(int i = 0 ; i < currentRepo.getSubmodules().length;i++){
            SubmoduleModel currentSubmodule = SubmoduleModel.builder()
                    .repositoryName(currentRepo.getSubmoduleRepositoryNames()[i])
                    .branchName(currentRepo.getSubmoduleRepositoryBranchNames()[i])
                    .commitSHA(currentRepo.getSubmoduleRepositoryCommits()[i])
                    .build();
            allSubmodulesOfCurrentRepo[i] = currentSubmodule;
        }

        return new ResponseEntity<SubmoduleModel[]>(allSubmodulesOfCurrentRepo, OK);
    }

    @PutMapping("/api/repository/{repositoryId}/edit")
    public ResponseEntity<CreateRepositoryModel> editRepository(@PathVariable String repositoryId, @RequestBody CreateRepositoryModel createRepositoryModel){

       // 1.) Delete Old Configuration Repository Locally
        RepositoryModel repoToDelete = repositoryParser.deleteRepository(repositoryId);

        // 2.) delete Old Configuration repository from github
        if(repoToDelete != null) {
            githubAPIService.deleteRepository(repoToDelete);
        }

        // 3.) Create new local configuration repository
        ResponseEntity createRepoResponse = githubAPIService.createRepository(createRepositoryModel);


       // 4.) Create new Configuration Repository using the git tree functions
        if(!createRepositoryModel.isContentRepository()) {
            githubAPIService.addSubmodulesToRepository(createRepositoryModel);
        }
        repositoryParser.saveNewConfiguration(createRepositoryModel);

        return new ResponseEntity<CreateRepositoryModel>(createRepositoryModel, OK);

    }

    @PutMapping("/api/repository/{repositoryId}/finalize")
    public ResponseEntity<RepositoryModel> finalizeRepository(@PathVariable String repositoryId){
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
    public ResponseEntity<RepositoryModel> deleteConfigurationRepository(@PathVariable String repositoryId){
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
    public ResponseEntity<BranchModel[]> getBranchesOfRepository(@PathVariable String repositoryId){
            ResponseEntity<BranchModel[]> responseEntity = githubAPIService.getBranchesOfRepository(repositoryId);
            if(responseEntity.getStatusCode()!=OK){
                return new ResponseEntity<>(BAD_REQUEST);
            }else {
                return new ResponseEntity<>(responseEntity.getBody(), OK);
            }
    }

    @GetMapping("/api/repository/{repositoryId}/branches/{branchName}/commits")
    public ResponseEntity<CommitModel[]> getBranchesOfRepository(@PathVariable String repositoryId, @PathVariable String branchName){
        ResponseEntity<CommitModel[]> responseEntity = githubAPIService.getCommitsOfBranchOfRepository(repositoryId,branchName);
        if(responseEntity.getStatusCode()!=OK){
            return new ResponseEntity<>(BAD_REQUEST);
        }else {
            return new ResponseEntity<>(responseEntity.getBody(), OK);
        }
    }

    @PostMapping("/api/repository")
    public ResponseEntity<CreateRepositoryModel> createRepository(@RequestBody CreateRepositoryModel createRepositoryModel){

        //if repository is already on github add it just in the JSON file
        if(!createRepositoryModel.isRepoAlreadyOnGithub()) {
            try {
                ResponseEntity createRepoResponse = githubAPIService.createRepository(createRepositoryModel);
            } catch (HttpClientErrorException e) {

                if (e.getStatusCode().equals(UNAUTHORIZED)) {
                    return new ResponseEntity<>(UNAUTHORIZED);
                } else if (e.getStatusCode().equals(UNPROCESSABLE_ENTITY)) {
                    return new ResponseEntity<>(UNPROCESSABLE_ENTITY);
                }
            }
            if (!createRepositoryModel.isContentRepository()) {
                githubAPIService.addSubmodulesToRepository(createRepositoryModel);
            }
        }

        repositoryParser.saveNewConfiguration(createRepositoryModel);

        return new ResponseEntity<CreateRepositoryModel>(createRepositoryModel, OK);
    }



}
