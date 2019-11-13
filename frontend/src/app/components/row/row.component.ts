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

  @Output() repoSelected = new EventEmitter();
  @Output() branchSelected = new EventEmitter();
  @Output() commitSelected = new EventEmitter();
  constructor(private backendApiService: BackendAPIService) {}

  ngOnInit() {}

  onSelectRepo(e) {
    this.repoSelected.emit({
      repoId: e.target.value,
      rowIndex: this.row.rowIndex
    });
  }

  onSelectBranch(e) {
    this.branchSelected.emit({
      rowIndex: this.row.rowIndex,
      branchName: e.target.value
    });
  }

  onSelectCommit(e) {
    this.commitSelected.emit({
      rowIndex: this.row.rowIndex,
      commitSHA: e.target.value
    });
  }
}
