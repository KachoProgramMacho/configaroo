import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.css"]
})
export class HeaderComponent implements OnInit {
  githubAccount: String;
  constructor() {}

  ngOnInit() {
    this.githubAccount = "";
    const currentAccount = localStorage.getItem("githubAccount");
    // If there is an account in localStorage set this in the app header to notify user's who owns the repositories
    if (currentAccount) {
      this.githubAccount = currentAccount;
    }
  }
}
