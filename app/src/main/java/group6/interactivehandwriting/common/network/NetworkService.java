package group6.interactivehandwriting.common.network;

import java.util.List;

import group6.interactivehandwriting.common.network.nearby.connections.NCDevice;

/**
 * Created by JakeL on 9/30/18.
 */

public interface NetworkService<T> {
    public void sendMessage(T message, List<NCDevice> device);
}
