package amos.group3.gitmodconfig_backend.controllers;


import amos.group3.gitmodconfig_backend.models.BranchModel;
import amos.group3.gitmodconfig_backend.models.CommitModel;
import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import amos.group3.gitmodconfig_backend.services.GithubAPIService;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

        @GetMapping("/api/repository/{repositoryId}/branches")
    public BranchModel[] getBranchesOfRepository(@PathVariable int repositoryId){
            return githubAPIService.getBranchesOfRepository(repositoryId);
    }

    @GetMapping("/api/repository/{repositoryId}/branches/{branchName}/commits")
    public CommitModel[] getBranchesOfRepository(@PathVariable int repositoryId, @PathVariable String branchName){
        return githubAPIService.getCommitsOfBranchOfRepository(repositoryId,branchName);
    }


}
