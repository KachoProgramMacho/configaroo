export class Repository {
  id: number;
  repo: string;
  type: string;
  url: string;
  owner: string;
  finalized: boolean;
  submodules: number[];
}
