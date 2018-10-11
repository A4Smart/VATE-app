package it.a4smart.vate.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import it.a4smart.vate.R;
import it.a4smart.vate.common.BeaconsFragment;

public class GuideFragment extends BeaconsFragment {
    private BeaconManager beaconManager;

    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);

        //Binding beacon manager to this activity
        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        beaconManager.bind(this);
        return view;
    }

    @Override
    public void handleNewBeacons(Collection<Beacon> beacons) {

    }
}
