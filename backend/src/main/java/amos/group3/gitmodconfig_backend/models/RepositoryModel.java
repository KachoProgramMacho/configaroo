package amos.group3.gitmodconfig_backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryModel {

    private String id;

    private String name;

    private String type;

    private String url;

    private String owner;

    private boolean finalized;

    private String[] submodules;

    private String[] submoduleRepositoryNames;

    private String[] submoduleRepositoryBranchNames;

    private String[] submoduleRepositoryCommits;


}
