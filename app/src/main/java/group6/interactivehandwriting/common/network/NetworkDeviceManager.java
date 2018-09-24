package group6.interactivehandwriting.common.network;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JakeL on 9/23/18.
 */

public class NetworkDeviceManager {
    private List<NetworkDeviceEntry> entries;

    public NetworkDeviceManager() {
        entries = new ArrayList<>();
    }

    public List<NetworkDeviceEntry> getDeviceEntries() {
        return entries;
    }

    public Boolean addDevice(String deviceName, Status connectionStatus) {
        Boolean isDeviceAdded = false;

        int statusCode = connectionStatus.getStatusCode();
        if (statusCode == ConnectionsStatusCodes.STATUS_OK) {
           isDeviceAdded = addDeviceIfNew(deviceName);
        }

        return isDeviceAdded;
    }

    private Boolean addDeviceIfNew(String deviceName) {
        Boolean isDeviceAdded = false;
        if (!isRegistered(deviceName)) {
            registerDevice(deviceName);
            isDeviceAdded = true;
        }
        return isDeviceAdded;
    }

    private Boolean isRegistered(String deviceName) {
        return entries.contains(new NetworkDeviceEntry(deviceName));
    }

    private void registerDevice(String deviceName) {
        entries.add(new NetworkDeviceEntry(deviceName));
    }

    public Boolean disconnectDevice(String deviceName) {
        Boolean deviceWasPresent;
        deviceWasPresent = entries.remove(new NetworkDeviceEntry(deviceName));
        return deviceWasPresent;
    }

    public Boolean shouldAcceptConnection(String deviceName) {
        if (!isRegistered(deviceName)) {
            return true;
        }
        else {
            return false;
        }
    }

    // TODO for now this method only returns all the devices it knows
    public List<String> getNeighboringDeviceNames() {
        List<String> deviceNames = new ArrayList<>();

        for (NetworkDeviceEntry entry : entries) {
            deviceNames.add(entry.getDeviceName());
        }

        return deviceNames;
    }
}
