import { Component } from "@angular/core";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent {
  choice: string = "random";

  changeValue = e => {
    this.choice = e.target.value;
  };
}
