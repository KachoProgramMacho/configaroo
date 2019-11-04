package amos.group3.gitmodconfig_backend.controllers;

import amos.group3.gitmodconfig_backend.services.GithubAPIService;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private GithubAPIService githubAPIService;

    @GetMapping("/")
    public List getHomePage(){
        List result = githubAPIService.getRepositoryMetadata();
        return result;
    }
}
