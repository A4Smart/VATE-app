package it.a4smart.vate;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

public class VATE extends Application implements BootstrapNotifier {
        private static final String TAG = ".VATE";
        private RegionBootstrap regionBootstrap;
        private BackgroundPowerSaver backgroundPowerSaver;

        @Override
        public void onCreate() {
            super.onCreate();
            Log.d(TAG, "App started up");
            BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

            BeaconParser parser = new BeaconParser().setBeaconLayout(Constants.BEACONS_LAYOUT);

            beaconManager.getBeaconParsers().add(parser);

            Region region = new Region("VATE_background", Identifier.parse(Constants.VATE_UUID), null, null);
            regionBootstrap = new RegionBootstrap(this, region);

            backgroundPowerSaver = new BackgroundPowerSaver(this);
        }

        @Override
        public void didDetermineStateForRegion(int arg0, Region arg1) {

        }

        @Override
        public void didEnterRegion(Region arg0) {
            //TODO fix this, doesn't seems to work in background for some reasons
            Log.d(TAG, "didEnterRegion");
            sendNotification();
        }

        @Override
        public void didExitRegion(Region arg0) {

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
