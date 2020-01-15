package amos.group3.gitmodconfig_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmoduleModel {
    private String repositoryName;
    private String branchName;
    private String commitSHA;
}
