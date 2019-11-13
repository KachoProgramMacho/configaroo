package amos.group3.gitmodconfig_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ConfigurationRepositoryModel {
    private String name;

    private SubmoduleModel[] submodules;

    private boolean auto_init;
}
