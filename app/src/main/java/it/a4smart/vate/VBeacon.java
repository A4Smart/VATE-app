package it.a4smart.vate;

import org.altbeacon.beacon.Beacon;

import java.util.Objects;

public class VBeacon {
    private final String uuid;
    private final String major;
    private final String minor;
    private final int rssi;

    public VBeacon (Beacon beacon) {
        uuid = beacon.getId1().toString();
        major = beacon.getId2().toString();
        minor = beacon.getId3().toString();
        rssi = (int) beacon.getRunningAverageRssi();
    }

    public boolean isNear() {
        return rssi > Constants.BEACONS_THRESHOLD;
    }

    public boolean isVATE() {
        return uuid.equals(Constants.VATE_UUID);
    }

    public String getURL() {
        return Constants.WEB_ADDRESS + buildID();
    }

    private String buildID() {
        return major + "_" + minor;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public String toString() {
        return "{VATE:"+isVATE()+", ID:"+buildID() +", RSSI:" + rssi + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VBeacon vBeacon = (VBeacon) o;
        return Objects.equals(getUuid(), vBeacon.getUuid()) &&
                Objects.equals(getMajor(), vBeacon.getMajor()) &&
                Objects.equals(getMinor(), vBeacon.getMinor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getMajor(), getMinor());
    }
}
