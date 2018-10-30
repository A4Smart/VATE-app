package it.a4smart.vate.common;

public abstract class Constants {
    //UUID of VATEs beacons.
    public final static String VATE_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";

    //Base URL for the VATE app.
    public final static String CONTENT_ADDRESS = "https://vateapp.eu/";

    //Folder where to find guide graphs
    public static final String GRAPH_ADDRESS = "https://raw.githubusercontent.com/A4Smart/VATE-graphs/master/";

    /*
     * Beacon library layout (actually iBeacon):
     * id1 -> UUID
     * id2 -> Major
     * id3 -> Minor
     */
    public final static String BEACONS_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";

    //Script to retrieve TTS text from webpages
    public final static String TTS_SCRIPT = "(function() { return document.getElementById('tts_text').innerHTML; })();";

    //Threshold for the beacon to be considered near.
    public final static double BEACONS_THRESHOLD = -80.0;

    //Default notification channel for advertising
    public static final String CHANNEL_ADS = "VATE_ads_channel";
}
