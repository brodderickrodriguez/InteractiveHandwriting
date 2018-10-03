package group6.interactivehandwriting.common.network.endpoint;

public abstract class Endpoint {
    protected String name;

    public Endpoint() { }

    public Endpoint(String name) {
        this.name = name;
    }

    public String getName() { return name; }

}
