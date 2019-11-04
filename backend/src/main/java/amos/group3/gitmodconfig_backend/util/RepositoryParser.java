package amos.group3.gitmodconfig_backend.util;

import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class RepositoryParser {

    ArrayList<RepositoryModel> repositories;

    @PostConstruct
    public void readJSONRepositoryFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            repositories = new ArrayList<RepositoryModel>(Arrays.asList(objectMapper.readValue(new File("src\\main\\resources\\repositories.json"), RepositoryModel[].class)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printRepositories(){
        return repositories.toString();
    }
}
