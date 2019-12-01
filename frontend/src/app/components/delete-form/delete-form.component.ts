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
    // Get All Repositories and filter only the ones that are not finalized
    this.backendApiService.getRepositories().subscribe(repositories => {
      this.repositories = repositories.filter(repo => !repo.finalized);
    });
  }

  onDeleteRepo(e) {
    const repoId = e.target.value;
    this.backendApiService.deleteRepository(repoId).subscribe(repository => {
      this.repositories = this.repositories.filter(repo => repo.id != repoId);
      alert("Repository successfully deleted");
    });
  }

  onFinalizeRepo(e) {
    const repoId = e.target.value;
    this.backendApiService.finalizeRepository(repoId).subscribe(repository => {
      alert("Repository successfully Finalized");
    });
  }
}
