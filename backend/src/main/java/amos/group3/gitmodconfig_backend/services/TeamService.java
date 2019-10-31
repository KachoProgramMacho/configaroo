package amos.group3.gitmodconfig_backend.services;

import amos.group3.gitmodconfig_backend.models.TeamModel;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    public TeamModel getTeamModel(){
        TeamModel teamModel = TeamModel.builder().name("AMOS").membersCount(5).build();
        return teamModel;
    }
}
