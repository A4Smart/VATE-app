package it.a4smart.vate.guide.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.a4smart.vate.graph.Graph;

public class Parser {

    public static Graph parseJson(String json) throws JSONException {
        Graph graph = new Graph();

        JSONObject jsonGraph = new JSONObject(json).getJSONObject("graph");
        boolean directed = jsonGraph.getBoolean("directed");
        JSONArray nodes = jsonGraph.getJSONArray("nodes");

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject obj = nodes.getJSONObject(i);

            int id = obj.getInt("id");
            String uri = obj.getString("uri");

            graph.addVertex(id, uri);
        }

        JSONArray edges = jsonGraph.getJSONArray("edges");

        for (int i = 0; i < edges.length(); i++) {
            JSONObject obj = edges.getJSONObject(i);

            int from = obj.getInt("from");
            int to = obj.getInt("to");
            int weight = obj.getInt("weight");
            int dir = obj.getInt("dir");

            graph.addEdge(from, to, weight, dir);
            if(!directed) graph.addEdge(to, from, weight, dir);
        }

        return graph;
    }
}
