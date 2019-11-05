import { Component, OnInit } from "@angular/core";
import { Repository } from "src/app/models/Repository";
import { BackendAPIService } from "../../services/backend-api.service";
@Component({
  selector: "app-config-form",
  templateUrl: "./config-form.component.html",
  styleUrls: ["./config-form.component.css"]
})
export class ConfigFormComponent implements OnInit {
  repositories: Repository[];
  constructor(private backendApiService: BackendAPIService) {}

  ngOnInit() {
    this.backendApiService.getRepositories().subscribe(repositories => {
      console.log("poluchih", repositories);

      this.repositories = repositories;
    });
  }
}
