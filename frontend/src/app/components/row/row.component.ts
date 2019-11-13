import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { Repository } from "src/app/models/Repository";
import { Branch } from "src/app/models/Branch";
import { BackendAPIService } from "../../services/backend-api.service";
import { Commit } from "src/app/models/Commit";
import { Row } from "src/app/models/Row";

@Component({
  selector: "app-row",
  templateUrl: "./row.component.html",
  styleUrls: ["./row.component.css"]
})
export class RowComponent implements OnInit {
  @Input() repositories: Repository[];
  @Input() row: Row;

  @Output() isRepoSelected = new EventEmitter();

  branches: Branch[];
  commits: Commit[];

  constructor(private backendApiService: BackendAPIService) {}

  ngOnInit() {}

  onSelectRepo(e) {
    this.isRepoSelected.emit({
      repoId: e.target.value,
      rowIndex: this.row.rowIndex
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
