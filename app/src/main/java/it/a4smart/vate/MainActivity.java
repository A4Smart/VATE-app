package it.a4smart.vate;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{
    private final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private BeaconManager beaconManager;
    private FloatingActionButton fab;
    private WebView webView;
    private WebView supportWebView;
    private Beacon actual;
    private Beacon next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = findViewById(R.id.webView);
        supportWebView = new WebView(this);

        fab = findViewById(R.id.fab);

        //fab.setVisibility(View.VISIBLE);
        //webView.loadUrl("https://example.com/");

        permissionCheck();
        initBluetooth();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBeacon(next);
                fab.setVisibility(View.GONE);
            }
        });
    }

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

    private void initBluetooth() {
        beaconManager = BeaconManager.getInstanceForApplication(this);

        /*
         * The next instruction creates a parser specific for iBeacon:
         * id1 -> UUID
         * id2 -> Major
         * id3 -> Minor
         */
        BeaconParser parser = new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24");

        beaconManager.getBeaconParsers().add(parser);

        beaconManager.bind(this);
    }

    public String buildURL(String major, String minor) {
        return Constants.WEB_ADDRESS + major + "_" + minor + "/";
    }

    public void showBeacon(final Beacon beacon) {
        final String URL = buildURL(beacon.getId2().toString(), beacon.getId3().toString());

        Log.d(TAG, "showBeacon: "+URL);

        supportWebView.loadUrl(URL);
        supportWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl(URL);
                actual = beacon;
            }
        });


    }

    public Beacon nearestVATE(Collection<Beacon> beacons) {
        Beacon nearest = null;

        for (Beacon beacon : beacons) {
            if ((nearest==null ||  beacon.getRunningAverageRssi() > nearest.getRunningAverageRssi()) && isVATEBeacon(beacon)) {
                nearest = beacon;
            }
        }

        Log.d(TAG, "nearestVATE: "+nearest);
        return nearest;
    }

    public void updateBeacon(final Beacon beacon) {

        int height = (int) Math.floor(webView.getContentHeight() * getResources().getDisplayMetrics().density);
        int webViewHeight = webView.getMeasuredHeight();

        if(actual == null || webView.getScrollY() + webViewHeight >= height) {
            Log.d(TAG, "updateBeacon: end reached");
            showBeacon(beacon);
        }
        else if (next == null || !isEqualBeacon(next, beacon)) {
            Log.d(TAG, "updateBeacon: setting next");
            next = beacon;
            fab.setVisibility(View.VISIBLE);
        }
    }

    public boolean isEqualBeacon(Beacon a, Beacon b) {
        return a.getId2().toString().equals(b.getId2().toString()) && a.getId3().toString().equals(b.getId3().toString());
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                final Beacon nearest = nearestVATE(beacons);



                if(nearest!=null && (actual==null || !isEqualBeacon(nearest, actual))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateBeacon(nearest);
                        }
                    });
                }

            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, "onBeaconServiceConnect: ", e);
        }
    }

    public boolean isVATEBeacon(Beacon beacon) {
        return beacon.getId1().toString().equals(Constants.VATE_UUID);
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

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
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            MainActivity.super.onBackPressed(); //Close the application since we can do nothing without permissions
                        }

                    });
                    builder.show();
                }
            }
        }
    }

}
