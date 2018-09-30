package group6.interactivehandwriting.common.network;

/**
 * Created by JakeL on 9/22/18.
 */

public interface NetworkManager<T> {
    public void setNetworkService(NearbyConnectionsNetworkService service);

    public void receiveMessage(T serviceMessage);

    public void sendMessage(NetworkMessage message);
}
