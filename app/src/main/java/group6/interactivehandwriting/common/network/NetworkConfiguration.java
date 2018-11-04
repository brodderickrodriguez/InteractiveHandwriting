package group6.interactivehandwriting.common.network;

import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkLayerService;

/**
 * Created by JakeL on 10/18/18.
 */

public class NetworkConfiguration {
    ///////////////////////////////////////////////////////////////////////////
    // set this variable to select the low-level network layer service to use
    ///////////////////////////////////////////////////////////////////////////
    public static final Class NETWORK_LAYER_SERVICE_CLASS = NCNetworkLayerService.class;
}
