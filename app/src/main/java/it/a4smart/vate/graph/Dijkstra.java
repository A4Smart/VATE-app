package it.a4smart.vate.graph;

import android.util.SparseArray;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Dijkstra {
    private HashMap<Vertex, Vertex> predecessors;
    private HashMap<Vertex, Integer> distance;

    public Dijkstra() {
        distance = new HashMap<>();
        predecessors = new HashMap<>();
    }

    public void execute(Vertex source) {
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        PriorityQueue<Vertex> pq = new PriorityQueue<>(10, (o1, o2) -> getActDistance(o1) - getActDistance(o2));
        pq.add(source);
        distance.put(source, 0);
        while (!pq.isEmpty()) {
            source = pq.poll();
            SparseArray<Edge> adjacent = source.getNeighbours();

            for (int i = 0; i < adjacent.size(); i++) {
                Vertex target = adjacent.valueAt(i).getDestination();
                int oldDist = getActDistance(target);
                int newDist = getActDistance(source) + getDistance(source, target);

                if (oldDist > newDist) {
                    distance.put(target, newDist);
                    predecessors.put(target, source);
                    pq.add(target);
                }
            }
        }
    }

    private int getActDistance(Vertex vertex) {
        return distance.containsKey(vertex) ? distance.get(vertex) : Integer.MAX_VALUE;
    }

    private int getDistance(Vertex from, Vertex to) {
        return from.getNeighbours().get(to.getName()).getWeight();
    }

    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<>();
        Vertex step = target;
        if (predecessors.get(step) == null) return null;
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        Collections.reverse(path);
        return path;
    }
}