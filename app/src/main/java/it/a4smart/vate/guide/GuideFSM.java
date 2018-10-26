package it.a4smart.vate.guide;

import java.util.List;

import it.a4smart.vate.graph.Vertex;

public class GuideFSM {
    public final static int WRONG = -1;
    public final static int IDLING = 0;
    public final static int STARTING = 1;
    public final static int NEXT = 2;
    public final static int END = 3;

    private Vertex[] pathArr;
    private int position;
    private int last;

    void setPath(List<Vertex> path) {
        pathArr = path.toArray(new Vertex[0]);
        position = -1;
        last = pathArr.length - 1;
    }

    public int nextMove(int minor) {
        if (position < 0) {
            if (minor == pathArr[0].getName()) {
                position = 0;
                return STARTING;
            }
        } else if (position == last) {
            return END;
        } else {
            if (minor == pathArr[position].getName()) {
                return IDLING;
            } else if (minor == pathArr[position + 1].getName()) {
                position++;
                return NEXT;
            }
        }
        return WRONG;
    }

    boolean isReady() {
        return pathArr != null && position < last;
    }

}
