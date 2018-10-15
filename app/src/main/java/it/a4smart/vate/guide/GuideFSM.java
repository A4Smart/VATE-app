package it.a4smart.vate.guide;

public class GuideFSM {
    private int[] way = null;
    private int position;
    private int last;

    void setWay(int[] way) {
        this.way = way;
        position = -1;
        last = way.length - 1;
    }

    public String nextMove(int minor) {
        String out = "WRONG";

        if (position < 0) {
            if (minor == way[0]) {
                position = 0;
                out = "ENTERED THE WAY";
            }
        } else if (position == last) {
            out = "ARRIVED";
        } else {
            if (minor == way[position]) {
                out = "NOT MOVING";
            } else if (minor == way[position + 1]) {
                position++;
                out = "RIGHT DIRECTION";
            }
        }

        return out;
    }

    boolean isReady() {
        return way != null && position < way.length - 1;
    }

}
