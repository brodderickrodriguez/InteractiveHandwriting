package group6.interactivehandwriting.common.network.nearby.connections;

/**
 * Created by JakeL on 9/23/18.
 */

public class NCDevice {
    private String deviceName;

    public NCDevice(String name) {
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
       } else if (other instanceof NCDevice) {
           NCDevice otherEntry = (NCDevice) other;
           return deviceName.equals(otherEntry.getDeviceName());
       } else {
           return false;
       }
    }
}
