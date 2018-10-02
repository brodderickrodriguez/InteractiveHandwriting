package group6.interactivehandwriting.common.network;

import android.net.Network;

/**
 * Created by JakeL on 9/23/18.
 */

public class NetworkDeviceEntry {
    private String deviceName;

    public NetworkDeviceEntry(String name) {
        deviceName = name;
    }

    public void setDeviceName(String name) {
        deviceName = name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public boolean equals(Object other) {
       if (other == null) {
           return false;
       } else if (other == this) {
           return true;
       } else if (other instanceof NetworkDeviceEntry) {
           NetworkDeviceEntry otherEntry = (NetworkDeviceEntry) other;
           return deviceName.equals(otherEntry.getDeviceName());
       } else {
           return false;
       }
    }
}
