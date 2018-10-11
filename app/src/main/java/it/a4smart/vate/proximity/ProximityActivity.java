package it.a4smart.vate.proximity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import it.a4smart.vate.R;
import it.a4smart.vate.common.Constants;
import it.a4smart.vate.common.TTS;
import it.a4smart.vate.common.VBeacon;

public class ProximityActivity extends AppCompatActivity implements BeaconConsumer {
    private final static String TAG = "ProximityActivity";
    private TreeSet<VBeacon> beaconsSet = new TreeSet<>((o1, o2) -> (o2.getRssi() - o1.getRssi()));
    private ProximityVM viewModel;
    private PagerAdapter pagerAdapter;
    private BeaconManager beaconManager;
    private ViewPager viewPager;
    private FloatingActionButton ttsButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        //Creating viewModel for proximity
        viewModel = new ProximityVM();

        //Creating adapter connected to viewModel
        pagerAdapter = new ProximityAdapter(getSupportFragmentManager(), viewModel);

        //Creating the viewPager and binding it to the pagerAdapter
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        //Populating tabs with the viewPager
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //Setting up the TTS button
        ttsButton = findViewById(R.id.fab);
        ttsButton.setOnClickListener(view -> {
            String textToSay = viewModel.getTTSText(viewPager.getCurrentItem());
            TTS.getInstance().speak(textToSay);
        });

        //Binding beacon manager to this activity
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
    }

    private void handleNewBeacons(Collection<Beacon> beacons) {

        Iterator i = beacons.iterator();
        while (i.hasNext() && beaconsSet.size() < 3) {
            VBeacon beacon = new VBeacon((Beacon) i.next());
            if (beacon.isVATE() && beacon.isNear()) beaconsSet.add(beacon);
        }

        viewModel.setBeacons(beaconsSet);
        pagerAdapter.notifyDataSetChanged();
    }


    /**
     * This will be called when the beacon service is first connected.
     * It starts ranging and sets the handler.
     */
    @Override
    public void onBeaconServiceConnect() {

        beaconManager.addRangeNotifier((beacons, region) -> handleNewBeacons(beacons));

        Region region = new Region("VATE_proximity", Identifier.parse(Constants.VATE_UUID), null, null);


        try {
            beaconManager.startRangingBeaconsInRegion(region);
            Log.d(TAG, "onBeaconServiceConnect: STARTED SEARCHING BEACONS!");
        } catch (RemoteException e) {
            Log.e(TAG, "onBeaconServiceConnect: ", e);
            //TODO tell the user an error happened
        }
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) super.onBackPressed();
        else viewPager.setCurrentItem(0);
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
