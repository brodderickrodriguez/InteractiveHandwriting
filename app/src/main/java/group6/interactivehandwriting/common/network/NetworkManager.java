package group6.interactivehandwriting.common.network;


import group6.interactivehandwriting.common.network.endpoint.Endpoint;

/**
 * Created by JakeL on 9/22/18.
 */

public interface NetworkManager<T> {
    public void setNetworkService(NearbyConnectionsNetworkService service);

    public void receiveMessage(T serviceMessage, Endpoint endpoint);

    public void sendMessage(NetworkMessage message);

    public boolean onConnectionInitiated(Endpoint endpoint);

    public void onConnectionResult(Endpoint endpoint);

    public void onDisconnected(Endpoint endpoint);

}
