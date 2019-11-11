package amos.group3.gitmodconfig_backend.controllers;

import amos.group3.gitmodconfig_backend.services.GithubAPIService;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private GithubAPIService githubAPIService;

    @Value("${GITHUB_PERSONAL_TOKEN}")
    private String GITHUB_PERSONAL_TOKEN;

    @GetMapping("/")
    public List getHomePage(){
        List result = githubAPIService.getRepositoryMetadata();
        System.out.println(GITHUB_PERSONAL_TOKEN);
        return result;
    }

    @GetMapping("/createRepo")
    public String createRepo(){
        return githubAPIService.createRepository();
    }
}
