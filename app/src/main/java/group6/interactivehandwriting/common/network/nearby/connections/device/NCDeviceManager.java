package group6.interactivehandwriting.common.network.nearby.connections.device;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import group6.interactivehandwriting.common.network.nearby.connections.device.NCDevice;

/**
 * Created by JakeL on 9/23/18.
 */

public class NCDeviceManager {
    private List<NCDevice> entries;

    public NCDeviceManager() {
        entries = new ArrayList<>();
    }

    public Boolean addDevice(NCDevice device, Status connectionStatus) {
        Boolean isDeviceAdded = false;

        int statusCode = connectionStatus.getStatusCode();
        if (statusCode == ConnectionsStatusCodes.STATUS_OK) {
            isDeviceAdded = addDeviceIfNew(device);
        }

        return isDeviceAdded;
    }

    private Boolean addDeviceIfNew(NCDevice device) {
        Boolean isDeviceAdded = false;
        if (!isRegistered(device)) {
            registerDevice(device);
            isDeviceAdded = true;
        }
        return isDeviceAdded;
    }

    private Boolean isRegistered(NCDevice device) {
        return entries.contains(device);
    }

    private void registerDevice(NCDevice device) {
        entries.add(device);
    }

    public Boolean disconnectDevice(NCDevice device) {
        Boolean deviceWasPresent;
        deviceWasPresent = entries.remove(device);
        return deviceWasPresent;
    }

    public Boolean shouldAcceptConnection(NCDevice device) {
        if (!isRegistered(device)) {
            return true;
        }
        else {
            return false;
        }
    }

    // TODO for now this method only returns all the devices it knows
    public List<String> getNeighboringDeviceNames() {
        List<String> deviceNames = new ArrayList<>();

        for (NCDevice entry : entries) {
            deviceNames.add(entry.getDeviceName());
        }

        return deviceNames;
    }

    public List<NCDevice> getDevices() {
        return entries;
    }
}
