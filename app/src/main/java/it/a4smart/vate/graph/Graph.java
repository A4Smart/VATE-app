package it.a4smart.vate.graph;

import android.util.SparseArray;

public class Graph {
    private final SparseArray<Vertex> vertexes;

    public Graph() {
        this.vertexes = new SparseArray<>();
    }

    public void addVertex(int vertex, String uri) {
        vertexes.put(vertex, new Vertex(vertex, uri));
    }

    public void addEdge(int from, int to, int weight, int dir) {
        Edge edge = new Edge(vertexes.get(from), vertexes.get(to), weight, dir);
        vertexes.get(from).addNeighbour(edge);
    }

    public Vertex getVertex(int vertex) {
        return vertexes.get(vertex);
    }
}
