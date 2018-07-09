package it.a4smart.vate;

import org.altbeacon.beacon.Beacon;

/**
 * This class is bad: it would've been better making a composition or to extending
 * the Beacon class, but since it can't be done I'll stick with this for now.
 * It would be good to modify the original Beacon class to make it more useful.
 */

public class Utils {
    /**
     * Build the ID for the given beacon.
     * @param beacon Beacon to read data from.
     * @return The ID ready to be used.
     */
    public static String buildID(Beacon beacon) {
        return beacon.getId2().toString() + "_" + beacon.getId3().toString() + "/";
    }


    /**
     * Checks if the given beacon is a VATE beacon.
     * @param beacon The beacon to check.
     * @return TRUE if VATE.
     */
    public static boolean isVATEBeacon(Beacon beacon) {
        return beacon.getId1().toString().equals(Constants.VATE_UUID);
    }
}
