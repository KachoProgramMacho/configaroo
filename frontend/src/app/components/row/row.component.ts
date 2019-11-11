import { Component, OnInit } from "@angular/core";
import { Repository } from "src/app/models/Repository";
import { Branch } from "src/app/models/Branch";
import { BackendAPIService } from "../../services/backend-api.service";

@Component({
  selector: "app-row",
  templateUrl: "./row.component.html",
  styleUrls: ["./row.component.css"]
})
export class RowComponent implements OnInit {
  repositories: Repository[];
  branches: Branch[];
  constructor(private backendApiService: BackendAPIService) {}

  ngOnInit() {
    this.backendApiService.getRepositories().subscribe(repositories => {
      this.repositories = repositories;
    });
  }

  onSelectRepo(e) {
    const repoId = e.target.value;
    //1.) Send request to fetch all the branches for the given repo
    this.backendApiService.getBranchesOfRepo(repoId).subscribe(branches => {
      this.branches = branches;
    });
  }
}
