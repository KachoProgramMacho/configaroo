package amos.group3.gitmodconfig_backend.controllers;

import amos.group3.gitmodconfig_backend.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/")
    public String getHomePage(){

        return "Hello " + teamService.getTeamModel().getName() +" with " + teamService.getTeamModel().getMembersCount() + " members!";
    }
}
