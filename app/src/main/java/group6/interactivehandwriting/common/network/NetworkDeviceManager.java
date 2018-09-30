package group6.interactivehandwriting.common.network;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import group6.interactivehandwriting.common.network.endpoint.Endpoint;
import group6.interactivehandwriting.common.network.endpoint.NearbyConnectionsEndpoint;

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

    public Boolean addDevice(Endpoint endpoint) {
        NearbyConnectionsEndpoint ncEndpoint = (NearbyConnectionsEndpoint) endpoint;

        int statusCode = ncEndpoint.getConnectionResolution().getStatus().getStatusCode();
        if (statusCode == ConnectionsStatusCodes.STATUS_OK) {
           return addDeviceIfNew(ncEndpoint.getName());
        }

        return false;
    }

    private Boolean addDeviceIfNew(String deviceName) {

        if (!isRegistered(deviceName)) {
            registerDevice(deviceName);
            return true;
        }
        return false;
    }

    private Boolean isRegistered(String deviceName) {
        return entries.contains(new NetworkDeviceEntry(deviceName));
    }

    private void registerDevice(String deviceName) {
        entries.add(new NetworkDeviceEntry(deviceName));
    }

    public Boolean disconnectDevice(Endpoint endpoint) {
        Boolean deviceWasPresent;
        deviceWasPresent = entries.remove(new NetworkDeviceEntry(endpoint.getName()));
        return deviceWasPresent;
    }

    public Boolean shouldAcceptConnection(Endpoint endpoint) {
        return !isRegistered(endpoint.getName());
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
