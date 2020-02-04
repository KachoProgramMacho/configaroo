package amos.group3.gitmodconfig_backend.controllers;

import amos.group3.gitmodconfig_backend.services.GithubAPIService;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private GithubAPIService githubAPIService;

    @Value("${GITHUB_PERSONAL_TOKEN}")
    private String GITHUB_PERSONAL_TOKEN;

    @Value("${GITHUB_ACCOUNT_OWNER}")
    private String GITHUB_ACCOUNT_OWNER;

    @GetMapping("/")
    public List getHomePage(){
        List result = githubAPIService.getRepositoryMetadata();
        System.out.println(GITHUB_PERSONAL_TOKEN);
        return result;
    }

    @GetMapping("/api/github-account")
    public ResponseEntity<String> getGithubAccount(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","text/plain");
        return new  ResponseEntity<String>(GITHUB_ACCOUNT_OWNER, headers, HttpStatus.OK);
    }
}
