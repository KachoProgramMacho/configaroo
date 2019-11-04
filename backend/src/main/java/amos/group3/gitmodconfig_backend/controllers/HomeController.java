package amos.group3.gitmodconfig_backend.controllers;

import amos.group3.gitmodconfig_backend.services.TeamService;
import amos.group3.gitmodconfig_backend.util.RepositoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private RepositoryParser repositoryParser;

    @GetMapping("/")
    public String getHomePage(){
        return repositoryParser.printRepositories();
        //return "Hello " + teamService.getTeamModel().getName() +" with " + teamService.getTeamModel().getMembersCount() + " members!";
    }
}
