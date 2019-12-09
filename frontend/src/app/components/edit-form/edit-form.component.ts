import { Component, OnInit } from "@angular/core";
import { Repository } from "src/app/models/Repository";
import { BackendAPIService } from "../../services/backend-api.service";

@Component({
  selector: "app-delete-form",
  templateUrl: "./edit-form.component.html",
  styleUrls: ["./edit-form.component.css"]
})
export class EditFormComponent implements OnInit {
  repositories: Repository[];
  loadingDelete: boolean;
  loadingEdit: boolean;
  loadingFinalize: boolean;
  errorMessage: string;

  constructor(private backendApiService: BackendAPIService) {
    this.repositories = [];
    this.loadingDelete = false;
    this.loadingEdit = false;
    this.loadingFinalize = false;
    this.errorMessage = "";
  }

  ngOnInit() {
    // Get All Repositories and filter only the ones that are not finalized
    this.backendApiService.getRepositories().subscribe(
      repositories => {
        this.repositories = repositories.filter(repo => !repo.finalized);
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
    this.loadingDelete = true;
    const repoId = e.target.value;
    this.backendApiService.deleteRepository(repoId).subscribe(
      repository => {
        this.repositories = this.repositories.filter(repo => repo.id != repoId);
        this.loadingDelete = false;
        alert("Repository successfully deleted");
      },
      err => {
        this.errorMessage = err.message;
        setTimeout(() => {
          this.errorMessage = "";
        }, 5000);
      }
    );
  }

  onFinalizeRepo(e) {
    this.loadingFinalize = true;
    const repoId = e.target.value;
    this.backendApiService.finalizeRepository(repoId).subscribe(
      repository => {
        this.repositories = this.repositories.filter(repo => repo.id != repoId);
        this.loadingFinalize = false;
        alert("Repository successfully Finalized");
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
