package it.a4smart.vate.guide;

public class Routing {

    private static final int[] way = {1, 2, 3, 4};
    private static final int[] yaw = {4, 3, 2, 1};

    public static int[] getRoute(int start) {

        if (start == way[0]) return way;
        else if (start == yaw[0]) return yaw;

        return null;
    }
}
