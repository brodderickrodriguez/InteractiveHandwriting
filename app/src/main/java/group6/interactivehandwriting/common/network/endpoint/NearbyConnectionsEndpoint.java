package group6.interactivehandwriting.common.network.endpoint;


import com.google.android.gms.nearby.connection.ConnectionResolution;

public class NearbyConnectionsEndpoint extends Endpoint {

    private ConnectionResolution connectionResolution;

    public NearbyConnectionsEndpoint(String name) {
        super();
        this.name = name;
    }

    public NearbyConnectionsEndpoint(String name, ConnectionResolution connectionResolution) {
        this.name = name;
        this.connectionResolution = connectionResolution;
    }

    public ConnectionResolution getConnectionResolution() { return connectionResolution; }

}
