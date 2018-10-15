package it.a4smart.vate.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import it.a4smart.vate.R;
import it.a4smart.vate.common.BeaconsFragment;
import it.a4smart.vate.common.VBeacon;

public class GuideFragment extends BeaconsFragment {
    private final static String TAG = "GuideFragment";
    private GuideFSM guideFSM;
    private TextView textView;

    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);

        textView = view.findViewById(R.id.textView);

        guideFSM = new GuideFSM();

        return view;
    }

    @Override
    public void handleNewBeacons(Collection<Beacon> beacons) {
        try {
            VBeacon nearest = new VBeacon(Collections.max(beacons, (o1, o2) -> Double.compare(o1.getRunningAverageRssi(), o2.getRunningAverageRssi())));
            int minor = nearest.getMinor();
            int major = nearest.getMajor();
            if (major == 42 || minor == 10000) guide(major, minor);

        } catch (NoSuchElementException ignored) {
        }
    }

    private String out;

    private void guide(int major, int minor) {
        if (!guideFSM.isReady()) {
            int[] way = Routing.getRoute(major, minor);
            if (way != null) {
                guideFSM.setWay(way);
                out = "";
            }
        } else {
            out += "minor: " + minor + ", guide: " + guideFSM.nextMove(minor) + "\n";
            textView.setText(out);
        }
    }

}
