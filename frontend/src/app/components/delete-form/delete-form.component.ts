import { Component, OnInit } from "@angular/core";
import { Repository } from "src/app/models/Repository";
import { BackendAPIService } from "../../services/backend-api.service";

@Component({
  selector: "app-delete-form",
  templateUrl: "./delete-form.component.html",
  styleUrls: ["./delete-form.component.css"]
})
export class DeleteFormComponent implements OnInit {
  repositories: Repository[];

  constructor(private backendApiService: BackendAPIService) {
    this.repositories = [];
  }

  ngOnInit() {
    this.backendApiService
      .getConfigurationRepositoriesToDelete()
      .subscribe(repositories => {
        this.repositories = repositories;
      });
  }

  onDeleteRepo(e) {
    const repoId = e.target.value;
    this.backendApiService
      .deleteConfigurationRepository(repoId)
      .subscribe(repository => {
        this.repositories = this.repositories.filter(repo => repo.id != repoId);
        alert("Repository successfully deleted");
      });
  }
}
