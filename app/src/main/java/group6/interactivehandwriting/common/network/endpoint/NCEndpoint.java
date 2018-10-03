package group6.interactivehandwriting.common.network.endpoint;


import com.google.android.gms.nearby.connection.ConnectionResolution;

public class NCEndpoint extends Endpoint {

    private ConnectionResolution connectionResolution;

    public NCEndpoint(String name) {
        super();
        this.name = name;
    }

    public NCEndpoint(String name, ConnectionResolution connectionResolution) {
        this.name = name;
        this.connectionResolution = connectionResolution;
    }

    public ConnectionResolution getConnectionResolution() { return connectionResolution; }

}
