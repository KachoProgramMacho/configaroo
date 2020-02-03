export class Repository {
  id: number;
  name: string;
  type: string;
  url: string;
  owner: string;
  finalized: boolean;
  submodules: number[];
}
