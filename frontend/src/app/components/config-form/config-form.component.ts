import { Component, OnInit } from "@angular/core";
import { Row } from "src/app/models/Row";
import { BackendAPIService } from "../../services/backend-api.service";
import { Repository } from "src/app/models/Repository";
import { Branch } from "src/app/models/Branch";
import { Commit } from "src/app/models/Commit";
import { CreateRepositoryModel } from "src/app/models/CreateRepositoryModel";
import { Submodule } from "src/app/models/Submodule";

@Component({
  selector: "app-config-form",
  templateUrl: "./config-form.component.html",
  styleUrls: ["./config-form.component.css"]
})
export class ConfigFormComponent implements OnInit {
  rows: Row[];
  repositories: Repository[];
  selectedRepoId: string;
  repoName: string;
  isLoadingCreateRepository: boolean;
  isContentRepository: boolean;
  errorMessage: string;
  isLoadingRepositories: boolean;

  constructor(private backendApiService: BackendAPIService) {
    this.rows = [];
    this.repositories = [];
    this.isLoadingCreateRepository = false;
    this.isContentRepository = false;
    this.errorMessage = "";
    this.isLoadingRepositories = true;
  }

  ngOnInit() {
    this.backendApiService.getRepositories().subscribe(
      repositories => {
        this.repositories = repositories;
        this.isLoadingRepositories = false;
      },
      err => {
        this.errorMessage = err.message;
        setTimeout(() => {
          this.errorMessage = "";
        }, 5000);
      }
    );
  }

  onAddRow(e) {
    const firstRow = new Row(this.rows.length, [], [], "", "", "");
    this.rows.push(firstRow);
  }

  onRemoveRow(e) {
    if (this.rows.length > 0) {
      this.rows.pop();
    }
  }

  onCreateConfig(e) {
    this.isLoadingCreateRepository = true;
    const configRepoSubmodules = this.rows.map(row => {
      return new Submodule(
        row.selectedRepoName,
        row.selectedBranchName,
        row.selectedCommitSHA
      );
    });
    const newRepo = new CreateRepositoryModel(
      this.repoName,
      configRepoSubmodules,
      this.isContentRepository
    );
    console.log(newRepo);
    this.backendApiService.createRepository(newRepo).subscribe(
      storedConfiguration => {
        console.log("STORED CONFIGURATION:", storedConfiguration);
        alert("Repository successfully created");
        this.isLoadingCreateRepository = false;
      },
      err => {
        this.errorMessage = err.message;
        setTimeout(() => {
          this.errorMessage = "";
        }, 5000);
      }
    );
  }

  onRepoSelected({ repoName, rowIndex }) {
    //check if empty
    if(!repoName){
      return;
    }
    const repoId = this.getRepoIdByRepoName(repoName);
    //1.) Send request to fetch all the branches for the given repo
    this.backendApiService.getBranchesOfRepo(repoId).subscribe(
      branches => {
        console.log("tva sum zel: ");
        console.log(branches);
        const currentRow = this.rows[rowIndex];
        currentRow.selectedRepoName = repoName;
        currentRow.selectedBranchName = "";
        currentRow.selectedCommitSHA = "";
        currentRow.branches = branches;
      },
      err => {
        this.errorMessage = err.message;
        setTimeout(() => {
          this.errorMessage = "";
        }, 5000);
      }
    );
  }

  onBranchSelected({ branchName, rowIndex }) {
    //check if empty
    if(!branchName){
      return;
    }
    const repoId = this.getRepoIdByRepoName(
      this.rows[rowIndex].selectedRepoName
    );
    this.backendApiService.getCommitsOfRepo(repoId, branchName).subscribe(
      commits => {
        const currentRow = this.rows[rowIndex];
        currentRow.selectedBranchName = branchName;
        currentRow.selectedCommitSHA = "";
        currentRow.commits = commits;
      },
      err => {
        this.errorMessage = err.message;
        setTimeout(() => {
          this.errorMessage = "";
        }, 5000);
      }
    );
  }

  onCommitSelected({ commitSHA, rowIndex }) {
    //check if empty
    if(!commitSHA){
      return;
    }
    const currentRow = this.rows[rowIndex];
    currentRow.selectedCommitSHA = commitSHA;
  }

  onRepoNameChange(e) {
    this.repoName = e.target.value;
  }

  getRepoIdByRepoName(repoName) {
    const repoId = this.repositories.filter(repo => repo.name === repoName)[0]
      .id;

    return repoId;
  }
}
