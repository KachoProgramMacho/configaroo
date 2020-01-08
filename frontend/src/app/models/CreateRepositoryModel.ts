import { Submodule } from "./Submodule";

export class CreateRepositoryModel {
  name: string;
  isContentRepository: boolean;
  submodules: Submodule[];

  constructor(
    name: string,
    submodules: Submodule[],
    isContentRepository: boolean
  ) {
    this.name = name;
    this.submodules = submodules;
    this.isContentRepository = isContentRepository;
  }
}
