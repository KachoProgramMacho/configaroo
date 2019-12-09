import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { ConfigFormComponent } from "./components/config-form/config-form.component";
import { EditFormComponent } from "./components/edit-form/edit-form.component";

const routes: Routes = [
  { path: "", component: ConfigFormComponent },
  { path: "edit", component: EditFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
