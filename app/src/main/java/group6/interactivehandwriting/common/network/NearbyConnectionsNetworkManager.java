package group6.interactivehandwriting.common.network;

import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.common.network.endpoint.Endpoint;
import group6.interactivehandwriting.common.network.endpoint.NearbyConnectionsEndpoint;

/**
 * Created by JakeL on 9/30/18.
 */

public class NearbyConnectionsNetworkManager implements NetworkManager<Payload> {
    private NetworkDeviceManager networkDeviceManager;
    private NearbyConnectionsNetworkService networkService;


    public NearbyConnectionsNetworkManager() {
        networkDeviceManager = new NetworkDeviceManager();
    }


    @Override
    public void setNetworkService(NetworkService service) {
        networkService = (NearbyConnectionsNetworkService) service;
    }

    @Override
    public void receiveMessage(Payload serviceMessage, Endpoint endpoint) {

    }

    @Override
    public void sendMessage(NetworkMessage message) {

    }


    @Override
    public boolean onConnectionInitiated(Endpoint endpoint) {
        return networkDeviceManager.shouldAcceptConnection(endpoint);
    }


    @Override
    public void onConnectionResult(Endpoint endpoint) {
        NearbyConnectionsEndpoint ncEndpoint = (NearbyConnectionsEndpoint) endpoint;
        networkDeviceManager.addDevice(endpoint);
    }


    @Override
    public void onDisconnected(Endpoint endpoint) {
        networkDeviceManager.disconnectDevice(endpoint);
    }


}
