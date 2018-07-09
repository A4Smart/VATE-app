package it.a4smart.vate;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import java.util.Collection;

import static it.a4smart.vate.Utils.isVATEBeacon;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{
    private final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private BeaconManager beaconManager;

    private Beacon actual;
    private Beacon next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        permissionCheck();
        initBluetooth();

    }


    /**
     * Starts the bluetooth and beacon manager.
     */
    private void initBluetooth() {
        //TODO check if bluetooth is turned on

        beaconManager = BeaconManager.getInstanceForApplication(this);

        /*
         * The next instruction creates a parser specific for iBeacon:
         * id1 -> UUID
         * id2 -> Major
         * id3 -> Minor
         */
        BeaconParser parser = new BeaconParser().setBeaconLayout(Constants.BEACONS_LAYOUT);

        beaconManager.getBeaconParsers().add(parser);

        beaconManager.bind(this);
    }


    /**
     * Search for the nearest VATE beacon and returns it.
     * @param beacons Collection of beacons to search in.
     * @return Nearest beacon.
     */
    public Beacon nearestVATE(Collection<Beacon> beacons) {
        Beacon nearest = null;

        Log.d(TAG, "nearestVATE: -------------------printing beacons-----------------------");

        for (Beacon beacon : beacons) {
            Log.d(TAG, "nearestVATE: " + beacon.toString() + " rssi: " + beacon.getRunningAverageRssi());
            if ((nearest==null ||  beacon.getRunningAverageRssi() > nearest.getRunningAverageRssi()) && isVATEBeacon(beacon)) {
                nearest = beacon;
            }
        }

        Log.d(TAG, "nearestVATE: -------------------finished printing----------------------");
        return nearest;
    }


    /**
     * Checks if two Beacon object refers to the same beacon.
     * @return true if the two Beacons refers to the same.
     */
    public boolean isEqualBeacon(Beacon a, Beacon b) {
        return a.getId2().toString().equals(b.getId2().toString()) && a.getId3().toString().equals(b.getId3().toString());
    }


    private void handleNewBeacons(Collection<Beacon> beacons) {

        // here i have more than one beacon

        final Beacon nearest = nearestVATE(beacons);


        if(nearest!=null && (actual==null ||!isEqualBeacon( nearest, actual)) && (next == null || !isEqualBeacon(nearest, next)) && nearest.getRunningAverageRssi() > Constants.BEACONS_THRESHOLD) {
            Log.d(TAG, "new beacon: "+nearest + " rssi: " +nearest.getRunningAverageRssi());
            runOnUiThread(() -> {
                //here i have the nearest beacon
                //TODO do something with it

            });
        }

    }


    /**
     * This will be called when the beacon service is first connected.
     * It starts ranging and sets the handler.
     */
    @Override
    public void onBeaconServiceConnect() {

        beaconManager.addRangeNotifier((beacons, region) -> handleNewBeacons(beacons));

        try {
            Region region = new Region("VATE_ranging", Identifier.parse(Constants.VATE_UUID), null, null);
            beaconManager.startRangingBeaconsInRegion(region);
            Log.d(TAG, "onBeaconServiceConnect: STARTED SEARCHING BEACONS!");
        } catch (RemoteException e) {
            Log.e(TAG, "onBeaconServiceConnect: ", e);
        }
    }


    /**
     * Since SDK 23 we have to check for permission at runtime.
     * This method check if it's needed to ask for permissions,
     * and eventually call {@link #requestPermissions(String[], int)} ()}.
     */
    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    /**
     * Callback for the permission request.
     * @see android.support.v4.app.FragmentActivity#onRequestPermissionsResult(int, String[], int[])
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //TODO put those in @string
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons, so it will now close.");
                    builder.setOnDismissListener(dialog -> {
                        MainActivity.super.onBackPressed(); //Close the application since we can do nothing without permissions
                    });
                    builder.show();
                }
            }
        }
    }



    //Activity life management stuff

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }
}
