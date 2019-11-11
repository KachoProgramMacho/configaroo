import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-config-form",
  templateUrl: "./config-form.component.html",
  styleUrls: ["./config-form.component.css"]
})
export class ConfigFormComponent implements OnInit {
  numberOfRows: number;
  rows: number[]; //dummy variable for rendering lists

  constructor() {
    this.numberOfRows = 1;
    this.updateRows();
  }

  ngOnInit() {}

  onAddRow(e) {
    console.log("hehe");

    this.numberOfRows += 1;
    this.updateRows();
  }

  onRemoveRow(e) {
    if(this.numberOfRows > 0) {
      this.numberOfRows -= 1;
      this.updateRows();
    }
  }

  updateRows() {
    this.rows = Array(this.numberOfRows).fill(0);
  }
}
