package amos.group3.gitmodconfig_backend.util;

import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
    @Autowired
    ResourceLoader resourceLoader;

    @PostConstruct
    public void readJSONRepositoryFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Resource resource=resourceLoader.getResource("classpath:repositories.json");
            repositories = new ArrayList<RepositoryModel>(Arrays.asList(objectMapper.readValue(resource.getFile(), RepositoryModel[].class)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printRepositories(){
        return repositories.toString();
    }

    public ArrayList<RepositoryModel> getRepositories(){
        return this.repositories;
    }
}
