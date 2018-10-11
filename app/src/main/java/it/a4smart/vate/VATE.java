package it.a4smart.vate;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.service.ArmaRssiFilter;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import it.a4smart.vate.common.Constants;

/**
 * To manage notification and search for beacon in the background we need to edit our application
 * base class. Here we take care of searching beacon and eventually notify the user that a new
 * beacon has been found.
 */

public class VATE extends Application implements BootstrapNotifier {
    private static final String TAG = ".VATE";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App started up");

        //Setting parameters for the beacon manager
        setupBeaconManager();

        //This is the filter for our background beacon search. Beacons are filtered by their UUID
        Region region = new Region("VATE_background", Identifier.parse(Constants.VATE_UUID), null, null);

        //Starting background search with a custom filter
        regionBootstrap = new RegionBootstrap(this, region);

        //The AltBeacon library's documentation advise to create an instance of this class to greatly
        //reduce power consumption. This has to be done once, and the object is never accessed again.
        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    private void setupBeaconManager() {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        BeaconParser parser = new BeaconParser().setBeaconLayout(Constants.BEACONS_LAYOUT);
        beaconManager.getBeaconParsers().add(parser);
        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
    }

    @Override
    public void didDetermineStateForRegion(int arg0, Region arg1) {

    }

    /**
     * Function that is called when the user get near a beacon which corresponds the filter
     * description. Since we know the beacon is ours, we can just send a notification if the app
     * is not already open.
     */
    @Override
    public void didEnterRegion(Region arg0) {
        //TODO fix this, doesn't seems to always work in background, for some reasons
        //TODO check if the app is not already opened
        Log.d(TAG, "didEnterRegion");
        sendNotification();
    }

    @Override
    public void didExitRegion(Region arg0) {
        //TODO delete the notification if out of range
    }


    private void sendNotification() {
        //TODO make notifications compatible with API>=26, adding a notification channel
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("VATE")
                        .setContentText("A beacon is nearby.") //TODO put in @string
                        .setSmallIcon(R.mipmap.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
