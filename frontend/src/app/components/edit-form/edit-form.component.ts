import { Component, OnInit } from "@angular/core";
import { Repository } from "src/app/models/Repository";
import { BackendAPIService } from "../../services/backend-api.service";
import { Row } from "../../models/Row";
import { Submodule } from "../../models/Submodule";
import { CreateRepositoryModel } from "../../models/CreateRepositoryModel";

@Component({
  selector: "app-delete-form",
  templateUrl: "./edit-form.component.html",
  styleUrls: ["./edit-form.component.css"]
})
export class EditFormComponent implements OnInit {
  rows: Row[];
  repositories: Repository[];
  loadingDelete: boolean;
  loadingEdit: boolean;
  loadingFinalize: boolean;
  errorMessage: string;
  editedName: string;
  editedId: number;

  currentlyEditedRepo: Repository;

  constructor(private backendApiService: BackendAPIService) {
    this.rows = [];
    this.repositories = [];
    this.loadingDelete = false;
    this.loadingEdit = false;
    this.loadingFinalize = false;
    this.errorMessage = "";
    this.currentlyEditedRepo = null;
    this.editedName = "";
  }

  ngOnInit() {
    // Get All Repositories and filter only the ones that are not finalized
    this.backendApiService.getRepositories().subscribe(
      repositories => {
        this.repositories = repositories; //.filter(repo => !repo.finalized);
      },
      err => {
        this.errorMessage = err.message;
        setTimeout(() => {
          this.errorMessage = "";
        }, 5000);
      }
    );
  }

  onDeleteRepo(e) {
    if (window.confirm("Do you really want to delete the repository?")) {
      this.loadingDelete = true;
      const repoId = e.target.value;
      this.backendApiService.deleteRepository(repoId).subscribe(
        repository => {
          this.loadingDelete = false;
          alert("Repository successfully deleted!");
          window.location.reload();
        },
        err => {
          this.errorMessage = err.message;
          setTimeout(() => {
            this.errorMessage = "";
          }, 5000);
        }
      );
    }
  }

  onFinalizeRepo(e) {
    if (window.confirm("Do you really want to finalize the configuration?")) {
      this.loadingFinalize = true;
      const repoId = e.target.value;
      this.backendApiService.finalizeRepository(repoId).subscribe(
        repository => {
          //this.repositories = this.repositories.filter(repo => repo.id != repoId);
          this.loadingFinalize = false;
          alert("Repository successfully finalized!");
          window.location.reload();
        },
        err => {
          this.errorMessage = err.message;
          setTimeout(() => {
            this.errorMessage = "";
          }, 5000);
        }
      );
    }
  }

  onModalClick(e) {
    let indexOfEditedRepo = e.target.value;
    this.currentlyEditedRepo = this.repositories[indexOfEditedRepo];
    this.editedName = this.currentlyEditedRepo.name;
    this.editedId = this.currentlyEditedRepo.id;
    this.backendApiService
      .getSubmodulesOfRepository(this.currentlyEditedRepo.id)
      .subscribe(submodules => {
        console.log(submodules);
        submodules.forEach(submodule => {
          let subRepoId = this.repositories.filter(
            repo => repo.name === submodule.repositoryName
          )[0].id;
          let rowBranches;
          let rowCommits;
          this.backendApiService
            .getCommitsOfRepo(subRepoId, submodule.branchName)
            .subscribe(
              commits => {
                rowCommits = commits;
                this.backendApiService.getBranchesOfRepo(subRepoId).subscribe(
                  branches => {
                    rowBranches = branches;

                    this.rows.push(
                      new Row(
                        this.rows.length,
                        rowBranches,
                        rowCommits,
                        submodule.repositoryName,
                        submodule.branchName,
                        submodule.commitSHA
                      )
                    );
                  },
                  err => {
                    this.errorMessage = err.message;
                    setTimeout(() => {
                      this.errorMessage = "";
                    }, 5000);
                  }
                );
              },
              err => {
                this.errorMessage = err.message;
                setTimeout(() => {
                  this.errorMessage = "";
                }, 5000);
              }
            );
        });
      });
  }

  onEditRepoNameChange(e) {
    this.editedName = e.target.value;
    console.log(this.editedName);
  }

  onRepoSelected({ repoName, rowIndex }) {
    //check if empty
    if(!repoName){
      return;
    }
    //1.) Send request to fetch all the branches for the given repo
    let repoId = this.getRepoIdByRepoName(repoName)
    this.backendApiService.getBranchesOfRepo(repoId).subscribe(
      branches => {
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
    let repoId = this.getRepoIdByRepoName(this.rows[rowIndex].selectedRepoName)
    this.backendApiService
      .getCommitsOfRepo(repoId, branchName)
      .subscribe(
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

  onAddRow(e) {
    const firstRow = new Row(this.rows.length, [], [], "", "", "");
    this.rows.push(firstRow);
  }

  onRemoveRow(e) {
    if (this.rows.length > 0) {
      this.rows.pop();
    }
  }

  onModalSaveChanges(e) {
    const configRepoSubmodules = this.rows.map(row => {
      return new Submodule(
        row.selectedRepoName,
        row.selectedBranchName,
        row.selectedCommitSHA
      );
    });
    const newRepo = new CreateRepositoryModel(
      this.editedName,
      configRepoSubmodules,
      false
    );
    console.log(newRepo);
    this.backendApiService.editConfiguration(this.editedId, newRepo).subscribe(
      storedConfiguration => {
        console.log("EDITED CONFIGURATION:", storedConfiguration);
        window.location.reload();
      },
      err => {
        this.errorMessage = err.message;
        setTimeout(() => {
          this.errorMessage = "";
        }, 5000);
      }
    );
    this.rows = [];
  }

  onModalClose() {
    this.rows = [];
  }

  getRepoIdByRepoName(repoName) {
    const repoId = this.repositories.filter(repo => repo.name === repoName)[0]
      .id;

    return repoId;
  }
}
