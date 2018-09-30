package group6.interactivehandwriting.common.network;

import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.common.actions.StartDrawAction;

/**
 * Created by JakeL on 9/30/18.
 */

public class NearbyConnectionsNetworkManager implements NetworkManager<Payload> {
    @Override
    public void setNetworkService(NearbyConnectionsNetworkService service) {

    }

    @Override
    public void receiveMessage(Payload serviceMessage) {
    }

    @Override
    public void sendMessage(NetworkMessage message) {

    }
}
