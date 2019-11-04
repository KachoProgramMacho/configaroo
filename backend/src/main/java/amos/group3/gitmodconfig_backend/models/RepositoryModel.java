package amos.group3.gitmodconfig_backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

@Data
public class RepositoryModel {

    private int id;

    private String repo;

    private String type;

    private String url;

    private String owner;

    private int[] submodules;
}
