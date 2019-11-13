import { Submodule } from "./Submodule";

export class ConfigurationRepositoryModel {
  name: string;
  submodules: Submodule[];

  constructor(name: string, subModules: Submodule[]) {
    this.name = name;
    this.submodules = subModules;
  }
}
