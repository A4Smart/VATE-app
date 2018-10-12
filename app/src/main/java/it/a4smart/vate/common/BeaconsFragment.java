package it.a4smart.vate.common;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BeaconsFragment extends Fragment implements BeaconConsumer {
    private final static String TAG = "BeaconsFragment";

    private BeaconManager beaconManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding beacon manager to this activity
        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        beaconManager.bind(this);
    }

    abstract public void handleNewBeacons(Collection<Beacon> beacons);

    /**
     * This will be called when the beacon service is first connected.
     * It starts ranging and sets the handler.
     */
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier((beacons, region) -> handleNewBeacons(beacons));
        Region region = new Region("VATE_proximity", Identifier.parse(Constants.VATE_UUID), Identifier.parse("42"), null);

        try {
            beaconManager.startRangingBeaconsInRegion(region);
            Log.d(TAG, "onBeaconServiceConnect: STARTED SEARCHING BEACONS!");
        } catch (RemoteException e) {
            Log.e(TAG, "onBeaconServiceConnect: ", e);
            //TODO tell the user an error happened
        }
    }

    //Activity life management stuff

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }
}
