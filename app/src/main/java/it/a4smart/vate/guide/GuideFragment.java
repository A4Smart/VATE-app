package it.a4smart.vate.guide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import it.a4smart.vate.R;
import it.a4smart.vate.common.BeaconsFragment;

public class GuideFragment extends BeaconsFragment {
    private final static String TAG = "GuideFragment";

    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    //private static final int[] DEWAY = {1,4,5,8,10,11,13,14,15,18,19,20,21,24};
    private static final int[] DEWAY = {1, 2, 3, 4};

    private int actPos = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);

        return view;
    }

    @Override
    public void handleNewBeacons(Collection<Beacon> beacons) {
        Beacon nearest = null;
        for (Beacon beacon : beacons) {
            if (nearest == null) nearest = beacon;
            if (beacon.getRunningAverageRssi() > nearest.getRunningAverageRssi())
                nearest = beacon;
        }

        if (nearest != null) {
            guide(nearest.getId3().toInt());
        }
    }


    //Finite state machine!"
    private int dir = 0;

    void guide(int minor) {
        Log.d(TAG, "guide: minor: " + minor);
        if (dir == 0) {
            if (minor == DEWAY[0]) {
                dir = 1;
                Log.d(TAG, "guide: ENTERED THE WAY");
            } else if (minor == DEWAY[DEWAY.length - 1]) {
                actPos = DEWAY.length - 1;
                dir = -1;
                Log.d(TAG, "guide: ENTERED THE YAW");
            } else {
                Log.d(TAG, "guide: NOT PART OF WAY");
            }
        } else if (minor == DEWAY[actPos]) {
            Log.d(TAG, "nextStep: NOT MOVING");
        } else if (minor == DEWAY[0] || minor == DEWAY[DEWAY.length - 1]) {
            dir = 0;
            Log.d(TAG, "guide: ARRIVED");
        } else if (minor == DEWAY[actPos + dir]) {
            actPos += dir;
            Log.d(TAG, "nextStep: RIGHT DIRECTION");
        } else {
            Log.d(TAG, "nextStep: WRONG");
        }
    }
}
