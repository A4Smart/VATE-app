package it.a4smart.vate.guide;

public class Routing {

    private static final int[] office = {1, 2, 3, 4};
    private static final int[] office_r = {4, 3, 2, 1};
    private static final int[] sanmarco = {1, 2, 3, 4, 5, 8, 15, 28};
    private static final int[] sanmarco_r = {28, 15, 8, 5, 4, 3, 2, 1};


    public static int[] getRoute(int place, int start) {

        if (place == 42) {
            if (start == office[0]) return office;
            else if (start == office_r[0]) return office_r;
        } else if (place == 10000) {
            if (start == sanmarco[0]) return sanmarco;
            else if (start == sanmarco_r[0]) return sanmarco_r;
        }

        return null;
    }
}
