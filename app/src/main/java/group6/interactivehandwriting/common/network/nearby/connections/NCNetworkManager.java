package group6.interactivehandwriting.common.network.nearby.connections;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.common.network.NetworkManager;
import group6.interactivehandwriting.common.network.NetworkMessage;
import group6.interactivehandwriting.common.network.NetworkService;

/**
 * Created by JakeL on 9/30/18.
 */

public class NCNetworkManager implements NetworkManager<Payload> {
    private NCDeviceManager deviceManager;
    private NCNetworkService networkService;

    public NCNetworkManager() {
        deviceManager = new NCDeviceManager();
    }

    @Override
    public void setNetworkService(NetworkService service) {
        networkService = (NCNetworkService) service;
    }

    @Override
    public void receiveMessage(Payload serviceMessage, NCDevice device) {

    }

    @Override
    public void sendMessage(NetworkMessage message) {

    }

    @Override
    public boolean onConnectionInitiated(NCDevice device) {
        return deviceManager.shouldAcceptConnection(device);
    }

    @Override
    public void onConnectionResult(NCDevice device, Status connectionStatus) {
        deviceManager.addDevice(device, connectionStatus);
    }

    @Override
    public void onDisconnected(NCDevice device) {
        deviceManager.disconnectDevice(device);
    }


}
