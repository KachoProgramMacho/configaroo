package amos.group3.gitmodconfig_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreateRepositoryModel {
    private String name;

    private String owner;

    private SubmoduleModel[] submodules;

    private boolean auto_init;

    private boolean contentRepository;

    private boolean repoAlreadyOnGithub;
}