package group6.interactivehandwriting.common.network.nearby.connections.device;

import group6.interactivehandwriting.common.app.rooms.Room;

/**
 * Created by JakeL on 10/30/18.
 */

public class NCRoutingTableRecord {
    public long timeStamp;
    public int distance;
    public String nextHopEndpointId;
    public String username;
    public Room room;

    public NCRoutingTableRecord() {
        nextHopEndpointId = "";
        distance = -1;
        username = "";
        room = new Room();
        room.deviceId = 0;
        room.name = "";
        timeStamp = 0;
    }

    public void updateTime() {
        timeStamp = System.currentTimeMillis();
    }

    public boolean isNeighbor() {
        return nextHopEndpointId != null &&
                !nextHopEndpointId.isEmpty() &&
                distance == 1;
    }
}
