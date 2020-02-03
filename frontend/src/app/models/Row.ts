import { Branch } from "./Branch";
import { Commit } from "./Commit";

export class Row {
  rowIndex: number;
  selectedRepoName: string;
  selectedBranchName: string;
  selectedCommitSHA: string;
  branches: Branch[];
  commits: Commit[];

  constructor(
    rowIndex: number,
    branches: Branch[],
    commits: Commit[],
    selectedRepoName: string,
    selectedBranchName: string,
    selectedCommitSHA: string
  ) {
    this.rowIndex = rowIndex;
    this.branches = branches;
    this.commits = commits;
    this.selectedRepoName = selectedRepoName;
    this.selectedBranchName = selectedBranchName;
    this.selectedCommitSHA = selectedCommitSHA;
  }
}
