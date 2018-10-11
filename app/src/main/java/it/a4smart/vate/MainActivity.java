package it.a4smart.vate;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import it.a4smart.vate.common.TTS;
import it.a4smart.vate.guide.GuideFragment;
import it.a4smart.vate.proximity.ProximityFragment;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBluetooth();
        permissionCheck();
        TTS.createNewInstance(getApplicationContext());

        TabLayout tabLayout = findViewById(R.id.main_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    setFragment(getSupportFragmentManager(), ProximityFragment.newInstance());
                if (tab.getPosition() == 1)
                    setFragment(getSupportFragmentManager(), GuideFragment.newInstance());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setFragment(getSupportFragmentManager(), ProximityFragment.newInstance());
    }

    /**
     * Enables bluetooth if it's turned off, since we need it for beacons detection.
     */
    private void initBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
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
                //TODO tell the user what the permission is used for
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    /**
     * Callback for the permission request.
     * @see FragmentActivity#onRequestPermissionsResult(int, String[], int[])
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


    private static void setFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_fragment, fragment);
        ft.commit();
    }
}
