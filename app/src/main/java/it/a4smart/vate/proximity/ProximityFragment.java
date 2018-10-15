package it.a4smart.vate.proximity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import it.a4smart.vate.R;
import it.a4smart.vate.common.BeaconsFragment;
import it.a4smart.vate.common.TTS;
import it.a4smart.vate.common.VBeacon;

public class ProximityFragment extends BeaconsFragment {
    private final static String TAG = "ProximityFragment";
    private TreeSet<VBeacon> beaconsSet = new TreeSet<>((o1, o2) -> (o2.getRssi() - o1.getRssi()));
    private ProximityVM viewModel;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private FloatingActionButton ttsButton;
    private TTS tts;

    public static ProximityFragment newInstance() {
        return new ProximityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proximity, container, false);

        //Creating viewModel for proximity
        viewModel = new ProximityVM();

        //Creating adapter connected to viewModel
        pagerAdapter = new ProximityAdapter(getChildFragmentManager(), viewModel);

        //Creating the viewPager and binding it to the pagerAdapter
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        //Populating tabs with the viewPager
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Log.d(TAG, "onCreateView: trying to get TTS");
        tts = TTS.getInstance();

        //Setting up the TTS button
        if (tts.isEnabled()) {
            Log.d(TAG, "onCreateView: TTS enabled, setting up...");
            ttsButton = view.findViewById(R.id.fab);
            ttsButton.show();
            ttsButton.setOnClickListener(btnview -> {
                String textToSay = viewModel.getTTSText(viewPager.getCurrentItem());
                tts.speak(textToSay);
            });
        }

        return view;
    }


    @Override
    public void handleNewBeacons(Collection<Beacon> beacons) {
        Iterator i = beacons.iterator();
        while (i.hasNext() && beaconsSet.size() < 3) {
            VBeacon beacon = new VBeacon((Beacon) i.next());
            if (beacon.isNear()) beaconsSet.add(beacon);
        }

        viewModel.setBeacons(beaconsSet);
        pagerAdapter.notifyDataSetChanged();
    }
}
