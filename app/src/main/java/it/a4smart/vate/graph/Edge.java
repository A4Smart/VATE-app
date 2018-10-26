package it.a4smart.vate.graph;

public class Edge {
    private final Vertex source;
    private final Vertex destination;
    private final int weight;
    private final int dir;

    Edge(Vertex source, Vertex destination, int weight, int dir) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.dir = dir;
    }

    Vertex getDestination() {
        return destination;
    }

    int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + "_to_" + destination + "_in_" + weight;
    }
}
