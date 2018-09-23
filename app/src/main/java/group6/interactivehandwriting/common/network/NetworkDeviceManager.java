package group6.interactivehandwriting.common.network;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JakeL on 9/23/18.
 */

// TODO abstracts device and routing table management, update internals later
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
        if (!entries.contains(new NetworkDeviceEntry(deviceName))) {
            entries.add(new NetworkDeviceEntry(deviceName));
            isDeviceAdded = true;
        }
        return isDeviceAdded;
    }

    public Boolean disconnectDevice(String deviceName) {
        Boolean deviceWasPresent;
        deviceWasPresent = entries.remove(deviceName);
        return deviceWasPresent;
    }

    public Boolean shouldAcceptConnection(String deviceName) {
        if (!entries.contains(deviceName)) {
            return true;
        }
        else {
            return false;
        }
    }

    public List<String> getNeighboringDeviceNames() {
        List<String> deviceNames = new ArrayList<>();

        for (NetworkDeviceEntry entry : entries) {
            deviceNames.add(entry.getDeviceName());
        }

        return deviceNames;
    }
}
