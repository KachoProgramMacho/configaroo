package amos.group3.gitmodconfig_backend.util;

import amos.group3.gitmodconfig_backend.models.CreateRepositoryModel;
import amos.group3.gitmodconfig_backend.models.RepositoryModel;
import amos.group3.gitmodconfig_backend.models.SubmoduleModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class RepositoryParser {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${GITHUB_ACCOUNT_OWNER}")
    private String GITHUB_ACCOUNT_OWNER;

    private final String GITHUB_URL_PREFIX = "https://github.com/";

    private ArrayList<RepositoryModel> repositories;


    @PostConstruct
    public void readJSONRepositoryFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            repositories = new ArrayList<RepositoryModel>(Arrays.asList(objectMapper.readValue(new File("src/main/resources/repositories.json"), RepositoryModel[].class)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RepositoryModel getRepositoryById(String id){
        return repositories.stream().filter((RepositoryModel repository)->repository.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find repository with ID: "+id));

    }

    public RepositoryModel saveNewConfiguration(CreateRepositoryModel createRepositoryModel){
        String[] submoduleRepositoryNames= null;
        String[] submoduleBranchNames= null;
        String[] submoduleCommits= null;


        String[] submodules= new String[0];
        if(!createRepositoryModel.isContentRepository()) {

            submodules = Arrays.asList(createRepositoryModel.getSubmodules()).stream()
                    .map(submoduleModel -> this.getIdByRepositoryName(submoduleModel.getRepositoryName())).toArray(size -> new String[size]);


            submoduleRepositoryNames = new String[submodules.length];
            submoduleBranchNames = new String[submodules.length];
            submoduleCommits = new String[submodules.length];
            for (int i = 0; i < submodules.length; i++) {
                SubmoduleModel submoduleModel = createRepositoryModel.getSubmodules()[i];

                String repositoryName = submoduleModel.getRepositoryName();

                String branchName = submoduleModel.getBranchName();
                String commit = submoduleModel.getCommitSHA();
                submoduleRepositoryNames[i] = repositoryName;
                submoduleBranchNames[i] = branchName;
                submoduleCommits[i] = commit;
            }
        }
        RepositoryModel newRepo = RepositoryModel.builder()
                .name(createRepositoryModel.getName())
                .owner(createRepositoryModel.isRepoAlreadyOnGithub()? createRepositoryModel.getOwner() : GITHUB_ACCOUNT_OWNER)
                .id(generateId())
                .url(GITHUB_URL_PREFIX+(createRepositoryModel.isRepoAlreadyOnGithub()? createRepositoryModel.getOwner() : GITHUB_ACCOUNT_OWNER)+"/"+ createRepositoryModel.getName())
                .type(createRepositoryModel.isContentRepository() ? "content" : "configuration")
                .finalized(false)
                .submodules(submodules)
                .submoduleRepositoryNames(submoduleRepositoryNames)
                .submoduleRepositoryBranchNames(submoduleBranchNames)
                .submoduleRepositoryCommits(submoduleCommits)
                .build();
        repositories.add(newRepo);
        this.writeRepositoriesToJSONFile(repositories);
        return newRepo;
    }

    public RepositoryModel deleteRepository(String id){
        RepositoryModel repoToDelete = this.getRepositoryById(id);
        if(!repoToDelete.isFinalized() && repoToDelete.getType().equals("configuration")){
            repositories.remove(repoToDelete);
            this.writeRepositoriesToJSONFile(repositories);
            return repoToDelete;
        }else {
            return null;
        }
    }

    public RepositoryModel finalizeRepository(String id){
        RepositoryModel toBeFinalized = this.getRepositoryById(id);
        if(toBeFinalized==null){
            return null;
        }else {
            repositories.remove(toBeFinalized);
            toBeFinalized.setFinalized(true);
            repositories.add(toBeFinalized);
            writeRepositoriesToJSONFile(repositories);
        }
        return toBeFinalized;
    }

    public String getIdByRepositoryName(String name){
        try {
            return repositories.stream().filter(repositoryModel -> repositoryModel.getName().equals(name))
                    .findFirst().get().getId();
        }catch (NoSuchElementException e){
            return  name;
        }
    }

    public ArrayList<RepositoryModel> getNotFinalizedConfigurationRepositories(){
        return repositories.stream().filter(repositoryModel -> !repositoryModel.isFinalized())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String printRepositories(){
        return repositories.toString();
    }

    public ArrayList<RepositoryModel> getRepositories(){
        return this.repositories;
    }

    //return max id value +1
    private String generateId(){
        return UUID.randomUUID().toString();
    }

    private void writeRepositoriesToJSONFile(ArrayList<RepositoryModel> repositories){
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            objectMapper.writeValue(new File("src/main/resources/repositories.json"), repositories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
