import { Component, OnInit } from "@angular/core";
import { BackendAPIService } from "../../services/backend-api.service";

@Component({
  selector: "app-main",
  templateUrl: "./main.component.html",
  styleUrls: ["./main.component.css"]
})
export class MainComponent implements OnInit {
  constructor(private backendApiService: BackendAPIService) {}

  ngOnInit() {
    this.backendApiService.getGithubAccount().subscribe(githubAccount => {
      const currentItem = localStorage.getItem("githubAccount");
      //If there is no github account currently set or the account has been changed, update the localStorage of the browser
      if (!currentItem || currentItem !== githubAccount) {
        localStorage.setItem("githubAccount", githubAccount);
      }
    });
  }
}
