import { Submodule } from "./Submodule";

export class Repository {
  id: string;
  name: string;
  type: string;
  url: string;
  owner: string;
  finalized: boolean;
  submodules: string[];

  constructor(name: string, submodules: string[]) {
    this.id = "randomID123456789";
    this.name = name;
    this.type = "configuration";
    this.url = "";
    this.owner = "";
    this.finalized = false;
    this.submodules = submodules;
  }
}
