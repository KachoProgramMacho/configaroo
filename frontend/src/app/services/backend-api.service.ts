import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Repository } from "../models/Repository";
import { Branch } from "../models/Branch";
import { Commit } from "../models/Commit";
import { CreateRepositoryModel } from "../models/CreateRepositoryModel";
import { Submodule } from "../models/Submodule";

@Injectable({
  providedIn: "root"
})
export class BackendAPIService {
  backendUrl: string = "http://localhost:8080/api";

  constructor(private http: HttpClient) {}

  getRepositories(): Observable<Repository[]> {
    return this.http.get<Repository[]>(`${this.backendUrl}/repository`);
  }

  getBranchesOfRepo(repoId): Observable<Branch[]> {
    return this.http.get<Branch[]>(
      `${this.backendUrl}/repository/${repoId}/branches`
    );
  }

  getCommitsOfRepo(repoId, branchName): Observable<Commit[]> {
    return this.http.get<Commit[]>(
      `${this.backendUrl}/repository/${repoId}/branches/${branchName}/commits`
    );
  }

  createRepository(
    newConfigurationRepository: CreateRepositoryModel
  ): Observable<CreateRepositoryModel> {
    return this.http.post<CreateRepositoryModel>(
      `${this.backendUrl}/repository`,
      newConfigurationRepository
    );
  }

  deleteRepository(id: number): Observable<Repository> {
    return this.http.delete<Repository>(`${this.backendUrl}/repository/${id}`);
  }

  finalizeRepository(id: number): Observable<Repository> {
    return this.http.put<Repository>(
      `${this.backendUrl}/repository/${id}/finalize`,
      {}
    );
  }

  getSubmodulesOfRepository(repoId: string): Observable<Submodule[]> {
    return this.http.get<Submodule[]>(
      `${this.backendUrl}/repository/${repoId}/submodules`
    );
  }

  editConfiguration(
    repoId: string,
    editRepository: CreateRepositoryModel
  ): Observable<Submodule[]> {
    return this.http.put<Submodule[]>(
      `${this.backendUrl}/repository/${repoId}/edit`,
      editRepository
    );
  }

  getGithubAccount(): Observable<string> {
    return this.http.get(`${this.backendUrl}/github-account`, {
      responseType: "text"
    });
  }
}
