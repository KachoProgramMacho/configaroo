import { Submodule } from "./Submodule";

export class CreateRepositoryModel {
  name: string;
  contentRepository: boolean;
  submodules: Submodule[];

  constructor(
    name: string,
    submodules: Submodule[],
    isContentRepository: boolean
  ) {
    this.name = name;
    this.submodules = submodules;
    this.contentRepository = isContentRepository;
  }
}
