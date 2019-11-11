import { Component, OnInit } from "@angular/core";
import { Repository } from "src/app/models/Repository";
import { Branch } from "src/app/models/Branch";
import { BackendAPIService } from "../../services/backend-api.service";
import { Commit } from "src/app/models/Commit";

@Component({
  selector: "app-row",
  templateUrl: "./row.component.html",
  styleUrls: ["./row.component.css"]
})
export class RowComponent implements OnInit {
  repositories: Repository[];
  branches: Branch[];
  commits: Commit[];

  selectedRepoId: string;
  selectedBranchName: string;
  selectedCommitIndex: number;

  constructor(private backendApiService: BackendAPIService) {
    this.selectedRepoId = "";
    this.selectedBranchName = "";
  }

  ngOnInit() {
    this.backendApiService.getRepositories().subscribe(repositories => {
      this.repositories = repositories;
    });
  }

  onSelectRepo(e) {
    const repoId = e.target.value;
    this.selectedRepoId = repoId;
    //1.) Send request to fetch all the branches for the given repo
    this.backendApiService.getBranchesOfRepo(repoId).subscribe(branches => {
      this.branches = branches;
    });
  }

  onSelectBranch(e) {
    const branchName = e.target.value;
    this.selectedBranchName = branchName;
    this.backendApiService
      .getCommitsOfRepo(this.selectedRepoId, branchName)
      .subscribe(commits => {
        this.commits = commits;
        console.log(this.commits);
      });
  }
}
