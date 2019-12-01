import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Repository } from "../models/Repository";
import { Branch } from "../models/Branch";
import { Commit } from "../models/Commit";
import { ConfigurationRepositoryModel } from "../models/ConfigurationRepositoryModel";

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
    console.log("WTF", repoId);

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
    newConfigurationRepository: ConfigurationRepositoryModel
  ): Observable<ConfigurationRepositoryModel> {
    return this.http.post<ConfigurationRepositoryModel>(
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
}
