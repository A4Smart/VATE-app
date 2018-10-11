package it.a4smart.vate.proximity;

import java.util.ArrayList;
import java.util.TreeSet;

import it.a4smart.vate.common.VBeacon;

public class ProximityVM {
    private TreeSet<VBeacon> beacons;
    private ArrayList<ProximityFragment> fragments;

    public ProximityVM() {
        fragments = new ArrayList<>(3);
    }

    int getBeaconsNumber() {
        return fragments.size();
    }

    ProximityFragment getFragment(int position) {
        return fragments.get(position);
    }

    public void setBeacons(TreeSet<VBeacon> set) {
        beacons = set;

        for (VBeacon beacon : beacons) {
            if (!fragmentAlreadyCreated(beacon.getID()))
                fragments.add(ProximityFragment.newInstance(beacon));
        }
    }

    private boolean fragmentAlreadyCreated(String id) {
        for (ProximityFragment fragment : fragments) if (fragment.getID().equals(id)) return true;
        return false;
    }

    public String getTTSText(int position) {
        return fragments.get(position).getTTSText();
    }
}
