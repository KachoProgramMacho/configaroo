import { Component, Input, OnInit } from "@angular/core";
import { Repository } from "../../models/Repository";
import ElGrapho from "elgrapho";

@Component({
  selector: "sigma-graph",
  templateUrl: "./sigma-graph.component.html",
  styleUrls: ["./sigma-graph.component.css"]
})
export class SigmaGraphComponent implements OnInit {
  @Input()
  repositories: Repository[];
  constructor() {}

  ngOnInit() {
    let model = this.generateRepositoryGraph(this.repositories);
    let graph = new ElGrapho({
      container: document.getElementById("sigma-container"),
      model: ElGrapho.layouts.Chord(model),
      width: 400,
      height: 400
    });
  }

  generateRepositoryGraph(repositories) {
    const nodes = [];
    const edges = [];
    let idToIndexMap = new Map();

    //map position in nodes to ID
    repositories.forEach((repository, index) => {
      idToIndexMap.set(repository.id, index);
    });

    repositories.forEach((repository, index) => {
      const repositoryNode = {
        label: repository.repo,
        x: Math.random(),
        y: Math.random(),
        group: repository.type === "content" ? 3 : 5
      };
      console.log("repository", repository);
      for (let i = 0; i < repository.submodules.length; i++) {
        const currentSubmoduleId = repository.submodules[i];
        const repositoryEdge = {
          to: idToIndexMap.get(repository.id),
          from: idToIndexMap.get(currentSubmoduleId)
        };
        edges.push(repositoryEdge);
      }

      nodes.push(repositoryNode);
    });
    return {
      nodes: nodes,
      edges: edges
    };
  }
}
