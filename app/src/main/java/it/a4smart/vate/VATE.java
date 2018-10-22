package it.a4smart.vate;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import androidx.core.app.NotificationCompat;
import it.a4smart.vate.common.Constants;

/**
 * To manage notification and search for beacon in the background we need to edit our application
 * base class. Here we take care of searching beacon and eventually notify the user that a new
 * beacon has been found.
 */

public class VATE extends Application implements BootstrapNotifier {
    private static final String TAG = ".VATE";
    private static final int NOTIFICATION_ID = 42;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App started up");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();

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
        //BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(5000);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ADS, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean isInForeground() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isInForeground", false);
    }

    private void sendNotification() {
        Notification notification = new NotificationCompat.Builder(this, Constants.CHANNEL_ADS)
                .setContentTitle("VATE")
                .setContentText("Attività ed eventi intorno a te!")
                .setSmallIcon(R.drawable.ic_notification)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {

    }

    /**
     * Function that is called when the user get near a beacon which corresponds the filter
     * description. Since we know the beacon is ours, we can just send a notification if the app
     * is not already open.
     */
    @Override
    public void didEnterRegion(Region region) {
        if (!isInForeground()) sendNotification();
    }

    @Override
    public void didExitRegion(Region region) {
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
