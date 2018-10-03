package group6.interactivehandwriting.common.network;

import com.google.android.gms.common.api.Status;

import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.network.nearby.connections.NCDevice;

/**
 * Created by JakeL on 9/22/18.
 */

public interface NetworkManager<T> {
    public void receiveMessage(T serviceMessage, NCDevice device);

    public void sendMessage(NetworkMessage message);

    public boolean onConnectionInitiated(NCDevice device);

    public void onConnectionResult(NCDevice device, Status connectionStatus);

    public void onDisconnected(NCDevice device);

    public void setCanvasManager(CanvasManager canvasManager);
}
