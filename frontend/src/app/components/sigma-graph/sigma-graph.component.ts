import { Component, Input, OnInit } from "@angular/core";
import sigma from "sigma";
import { Repository } from "../../models/Repository";

@Component({
  selector: "sigma-graph",
  templateUrl: "./sigma-graph.component.html",
  styleUrls: ["./sigma-graph.component.css"]
})
export class SigmaGraphComponent implements OnInit {
  private sigma: any;

  @Input()
  repositories: Repository[];

  private graph = {
    nodes: [
      { id: "n0", label: "A node", x: 0, y: 0, size: 3, color: "#008cc2" },
      {
        id: "n1",
        label: "Another node",
        x: 3,
        y: 1,
        size: 2,
        color: "#008cc2"
      },
      {
        id: "n2",
        label: "And a last one",
        x: 1,
        y: 3,
        size: 1,
        color: "#E57821"
      }
    ],
    edges: [
      {
        id: "e0",
        source: "n0",
        target: "n1",
        color: "#282c34",
        type: "line",
        size: 0.5
      },
      {
        id: "e1",
        source: "n1",
        target: "n2",
        color: "#282c34",
        type: "curve",
        size: 1
      },
      {
        id: "e2",
        source: "n2",
        target: "n0",
        color: "#FF0000",
        type: "line",
        size: 2
      }
    ]
  };
  constructor() {}

  ngOnInit() {
    this.sigma = new sigma({
      renderer: {
        container: document.getElementById("sigma-container"),
        type: "canvas"
      },
      settings: {}
    });
    this.sigma;
    this.graph = this.generateRepositoryGraph(this.repositories);
    this.sigma.graph.read(this.graph);
    this.sigma.refresh();
    console.log(this.sigma);
  }

  generateRepositoryGraph(repositories) {
    const nodes = [];
    const edges = [];
    repositories.forEach((repository, index) => {
      const repositoryNode = {
        id: repository.id,
        label: repository.repo,
        x: Math.random(),
        y: Math.random(),
        size: 5,
        color: repository.type === "content" ? "#69f0ae" : "#f50057"
      };
      console.log("repository", repository);
      for (let i = 0; i < repository.submodules.length; i++) {
        const currentSubmoduleId = repository.submodules[i];
        const repositoryEdge = {
          id: `e${repository.id}${currentSubmoduleId}`,
          source: repository.id,
          target: currentSubmoduleId,
          color: "#FF0000",
          type: "line",
          size: 2
        };
        edges.push(repositoryEdge);
      }

      nodes.push(repositoryNode);
    });
    return {
      nodes,
      edges
    };
  }
}
