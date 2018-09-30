package group6.interactivehandwriting.common.network;

/**
 * Created by JakeL on 9/30/18.
 */

public interface NetworkService<T> {
    public void setNetworkManager(NetworkManager<T> manager);

    public void sendMessage(T message);
}
