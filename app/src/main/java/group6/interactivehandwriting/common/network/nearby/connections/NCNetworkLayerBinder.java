package group6.interactivehandwriting.common.network.nearby.connections;

import group6.interactivehandwriting.common.network.NetworkLayer;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;

/**
 * Created by JakeL on 10/17/18.
 */

public class NCNetworkLayerBinder extends NetworkLayerBinder {
    private NetworkLayer networkLayer;

    public NCNetworkLayerBinder(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
    }

    @Override
    public NetworkLayer getNetworkLayer() {
        return networkLayer;
    }
}
