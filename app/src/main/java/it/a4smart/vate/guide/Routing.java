package it.a4smart.vate.guide;

import java.util.List;

import it.a4smart.vate.common.Constants;
import it.a4smart.vate.graph.Dijkstra;
import it.a4smart.vate.graph.Graph;
import it.a4smart.vate.graph.Vertex;

public class Routing {

    private static final int[] office = {1, 2, 3, 4};
    private static final int[] sanmarco = {1, 2, 3, 4, 5, 8, 15, 28};


    public static List<Vertex> getRoute(int place, int from, int to) {
        Graph graph = new Graph();
        populateGraph(graph, place);
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.execute(graph.getVertex(from));
        return dijkstra.getPath(graph.getVertex(to));
    }

    private static void populateGraph(Graph graph, int place) {
        int[] way = null;
        if (place == 42) way = office;
        else if (place == 10000) way = sanmarco;

        for (int i : way) graph.addVertex(i, Constants.WEB_ADDRESS + place + "_" + i + "24");
        for (int i = 0; i < way.length - 1; i++) {
            graph.addEdge(way[i], way[i + 1], 100, 0);
            graph.addEdge(way[i + 1], way[i], 100, 0);
        }
    }
}
