import {
  Component,
  Input,
  OnInit,
  SimpleChanges,
  ChangeDetectionStrategy
} from "@angular/core";
import { Repository } from "../../models/Repository";
import ElGrapho from "elgrapho";
import { log } from "util";

@Component({
  selector: "graph",
  templateUrl: "./graph.component.html",
  styleUrls: ["./graph.component.css"],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GraphComponent implements OnInit {
  @Input() repositories: Repository[];
  constructor() {}

  //Initial graph type
  graphType: string = "Chord";

  ngOnInit() {
    this.createGraph();
  }

  onSelectGraphType(e) {
    this.graphType = e.target.value;
    this.createGraph();
  }

  createGraph() {
    let model = this.generateRepositoryGraph(this.repositories);
    new ElGrapho({
      container: document.getElementById("sigma-container"),
      model: ElGrapho.layouts[`${this.graphType}`](model),
      width: 400,
      height: 400
    });
  }

  ngOnChanges() {
    console.log("NESHTO STAVA KURCI");
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
        label: repository.name,
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
