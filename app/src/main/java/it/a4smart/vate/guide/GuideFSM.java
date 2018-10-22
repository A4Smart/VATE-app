package it.a4smart.vate.guide;

public class GuideFSM {
    public final static int WRONG = -1;
    public final static int IDLING = 0;
    public final static int STARTING = 1;
    public final static int NEXT = 2;
    public final static int END = 3;

    private int[] way = null;
    private int position;
    private int last;

    void setWay(int[] way) {
        this.way = way;
        position = -1;
        last = way.length - 1;
    }

    public int nextMove(int minor) {
        if (position < 0) {
            if (minor == way[0]) {
                position = 0;
                return STARTING;
            }
        } else if (position == last) {
            return END;
        } else {
            if (minor == way[position]) {
                return IDLING;
            } else if (minor == way[position + 1]) {
                position++;
                return NEXT;
            }
        }
        return WRONG;
    }

    boolean isReady() {
        return way != null && position < last;
    }

}
