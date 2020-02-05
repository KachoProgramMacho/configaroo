import { Submodule } from "./Submodule";

export class Repository {
  id: string;
  name: string;
  type: string;
  url: string;
  owner: string;
  finalized: boolean;
  submodules: string[];

  constructor(name: string, submodules: string[], id, type) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.url = "";
    this.owner = "";
    this.finalized = false;
    this.submodules = submodules;
  }
}
