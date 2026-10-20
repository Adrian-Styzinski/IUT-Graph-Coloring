# Graph Coloring: Algorithm Exploration (Java)

*Note: This project was developed outside GitHub and imported here for archival purposes (hence the absence of commit history).*

Java application that loads graphs from CSV files and colors them with three algorithms, so their results and running times can be compared. The Swing viewer was provided by the course instructors. Completed in pairs by Adrian Styzinski and Simon Foussat-Ferre for the SAE "Visualisation de graphe" (BUT Informatique, IUT de Bordeaux, 2025-2026).

## Features

- **CSV loading:** reads a node file (`id;x y`) and an edge file (`source;target`) with `GraphLoader`.
- **Three coloring algorithms:**
  - **Welsh & Powell:** greedy coloring, nodes sorted by decreasing degree.
  - **6-coloring (planar):** repeatedly removes a node of degree ≤ 5, then colors on the way back.
  - **5-coloring (planar):** recursive reduction of the graph, run in a dedicated thread with a larger stack to handle big graphs, guaranteeing at most 5 colors.
- **Swing viewer (provided):** window supplied for the project, used to load a dataset, run each algorithm with a button, and display the number of nodes, edges and the computation time.
- **Unit tests (JUnit 4):** tests for the graph structure and for each algorithm on reference graphs.
- **Datasets:** planar graphs of increasing size in `dataset/`.

## Requirements

- **Java JDK 21**
- **Apache Ant** or **NetBeans**
- JAR dependencies already included in `libs/`

## Build and Run

```bash
git clone https://github.com/Chocopter/PU-Graph-Coloring.git
cd PU-Graph-Coloring
ant jar
java -jar dist/SAE2-3.jar
```

Or open the folder in NetBeans and press F6.

## Skills Demonstrated

Object-oriented Java, graph algorithms, unit testing with JUnit.
