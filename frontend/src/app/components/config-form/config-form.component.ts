import { Component, OnInit } from "@angular/core";
import { Row } from "src/app/models/Row";
import { BackendAPIService } from "../../services/backend-api.service";
import { Repository } from "src/app/models/Repository";

@Component({
  selector: "app-config-form",
  templateUrl: "./config-form.component.html",
  styleUrls: ["./config-form.component.css"]
})
export class ConfigFormComponent implements OnInit {
  rows: Row[];
  repositories: Repository[];
  selectedRepoId: string;

  constructor(private backendApiService: BackendAPIService) {
    this.rows = [];
  }

  ngOnInit() {
    this.backendApiService.getRepositories().subscribe(repositories => {
      this.repositories = repositories;
    });
  }

  onRemoveRow(e) {
    if (1 > 0) {
    }
  }

  onCreateConfig(e) {}

  onRepoSelected(repoId: string, rowIndex: number) {
    console.log(repoId, rowIndex);

    // this.selectedRepoId = repoId;
    // //1.) Send request to fetch all the branches for the given repo
    // this.backendApiService.getBranchesOfRepo(repoId).subscribe(branches => {
    //   this.branches = branches;
    // });
  }

  onAddRow(e) {
    const firstRow = new Row(this.rows.length, "", "", 0);
    this.rows.push(firstRow);
  }
}
