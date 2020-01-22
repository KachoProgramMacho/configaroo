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
        row.selectedRepoId,
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

  onRepoSelected({ repoId, rowIndex }) {
    //1.) Send request to fetch all the branches for the given repo
    this.backendApiService.getBranchesOfRepo(repoId).subscribe(
      branches => {
        const currentRow = this.rows[rowIndex];
        currentRow.selectedRepoId = repoId;
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
    this.backendApiService
      .getCommitsOfRepo(this.rows[rowIndex].selectedRepoId, branchName)
      .subscribe(
        commits => {
          const currentRow = this.rows[rowIndex];
          currentRow.selectedBranchName = branchName;
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
    const currentRow = this.rows[rowIndex];
    currentRow.selectedCommitSHA = commitSHA;
  }

  onRepoNameChange(e) {
    this.repoName = e.target.value;
  }
}
