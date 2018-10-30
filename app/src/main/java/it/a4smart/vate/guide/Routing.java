package it.a4smart.vate.guide;

import org.json.JSONException;

import java.util.List;

import it.a4smart.vate.common.Constants;
import it.a4smart.vate.graph.Dijkstra;
import it.a4smart.vate.graph.Graph;
import it.a4smart.vate.graph.Vertex;
import it.a4smart.vate.guide.route.Parser;
import it.a4smart.vate.guide.route.Retriever;

class Routing {

    private static String getUrl(int place) {
        return Constants.GRAPH_ADDRESS + place + ".json";
    }

    static List<Vertex> getRoute(int place, int from, int to) {
        String json = Retriever.getBlocking(getUrl(place));
        Graph graph = null;

        try {
            graph = Parser.parseJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Dijkstra dijkstra = new Dijkstra();
        dijkstra.execute(graph.getVertex(from));
        return dijkstra.getPath(graph.getVertex(to));
    }



}
