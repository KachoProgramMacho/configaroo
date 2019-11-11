package amos.group3.gitmodconfig_backend.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRepositoryModel {
    private String name;
}
