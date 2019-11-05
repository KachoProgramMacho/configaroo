package amos.group3.gitmodconfig_backend.controllers;


import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class GithubAPIController {

    @Autowired
    private RepositoryParser repositoryParser;

    @GetMapping("/api/repository")
    public ArrayList<RepositoryModel> getRepositories(){
        return repositoryParser.getRepositories();
    }

    @GetMapping("/api/repository/{id}/branch")
    public ArrayList<RepositoryModel> getBranchesOfRepository(@PathVariable int id){



        return repositoryParser.getRepositories();
    }
}
