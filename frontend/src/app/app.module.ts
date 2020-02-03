import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { HeaderComponent } from "./components/header/header.component";
import { MainComponent } from "./components/main/main.component";
import { ConfigFormComponent } from "./components/config-form/config-form.component";
import { RowComponent } from "./components/row/row.component";
import { EditFormComponent } from "./components/edit-form/edit-form.component";
import { GraphComponent } from "./components/graph-component/graph.component";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MainComponent,
    ConfigFormComponent,
    RowComponent,
    EditFormComponent,
    GraphComponent
  ],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
