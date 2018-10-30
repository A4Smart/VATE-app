package it.a4smart.vate.graph;

import android.util.SparseArray;

import java.util.Objects;

public class Vertex {
    final private int name;
    final private String URI;
    final private SparseArray<Edge> neighbours;

    Vertex(int name, String uri) {
        URI = uri;
        neighbours = new SparseArray<>();
        this.name = name;
    }

    public int getName() {
        return name;
    }

    SparseArray<Edge> getNeighbours() {
        return neighbours;
    }

    void addNeighbour(Edge edge) {
        neighbours.put(edge.getDestination().getName(), edge);
    }

    @Override
    public String toString() {
        return "id: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(name, vertex.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getURI() {
        return URI;
    }
}
