import { Submodule } from "./Submodule";

export class CreateRepositoryModel {
  name: string;
  contentRepository: boolean;
  submodules: Submodule[];
  owner: string;
  repoAlreadyOnGithub: boolean;

  constructor(
    name: string,
    submodules: Submodule[],
    isContentRepository: boolean,
    repoAlreadyOnGithub?: boolean,
    owner?: string
  ) {
    this.name = name;
    this.submodules = submodules;
    this.contentRepository = isContentRepository;
    this.owner = owner;
    this.repoAlreadyOnGithub = repoAlreadyOnGithub;
  }
}
