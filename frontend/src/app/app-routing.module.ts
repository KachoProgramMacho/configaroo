import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { ConfigFormComponent } from "./components/config-form/config-form.component";
import { DeleteFormComponent } from "./components/delete-form/delete-form.component";

const routes: Routes = [
  { path: "", component: ConfigFormComponent },
  { path: "edit", component: DeleteFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
