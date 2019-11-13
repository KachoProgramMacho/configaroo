export class Row {
  rowIndex: number;
  selectedRepoId: string;
  selectedBranchName: string;
  selectedCommitIndex: number;

  constructor(
    rowIndex: number,
    selectedRepoId: string,
    selectedBranchName: string,
    selectedCommitIndex: number
  ) {
    this.rowIndex = rowIndex;
    this.selectedRepoId = selectedRepoId;
    this.selectedBranchName = selectedBranchName;
    this.selectedCommitIndex = selectedCommitIndex;
  }
}
