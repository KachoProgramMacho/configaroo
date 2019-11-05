import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Repository } from "../models/Repository";
import { Branch } from "../models/Branch";

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
}
