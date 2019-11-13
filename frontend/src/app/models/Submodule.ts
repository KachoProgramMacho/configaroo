export class Submodule {
  repositoryName: string;
  branchName: string;
  commitSHA: string;

  constructor(repositoryName: string, branchName: string, commitSHA: string) {
    this.repositoryName = repositoryName;
    this.branchName = branchName;
    this.commitSHA = commitSHA;
  }
}
