package group6.interactivehandwriting.common.network.nearby.connections.device;

import android.support.annotation.NonNull;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.rooms.Room;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;

/**
 * Created by JakeL on 9/23/18.
 */

public class NCRoutingTable implements Iterable<Long> {
    private Map<Long, NCRoutingTableRecord> table;

    public NCRoutingTable() {
        table = new Hashtable<>();
    }

    public void setMyProfile(final Profile p) {
        NCRoutingTableRecord record = new NCRoutingTableRecord();
        record.distance = 0;
        record.username = p.username;
        record.updateTime();
        table.put(p.deviceId, record);
    }

    public void setMyRoom(final Profile myProfile, final Room myRoom) {
        setMyProfile(myProfile);
        NCRoutingTableRecord record = table.get(myProfile.deviceId);
        record.room = new Room();
        record.room.deviceId = myRoom.deviceId;
        record.room.name = myRoom.name;
        table.put(myProfile.deviceId, record);
    }

    public void exitMyRoom(final Profile profile) {
        setMyProfile(profile);
        table.get(profile).room = new Room(); // set to empty room
    }

    public void update(String endpointId, NCRoutingTable otherTable) {
        for (Long deviceId : otherTable) {
            NCRoutingTableRecord staleRecord = table.get(deviceId);
            if (staleRecord == null) {
                insertRecord(endpointId, deviceId, otherTable.get(deviceId));
            } else {
                updateRecord(endpointId, deviceId, otherTable.get(deviceId));
            }
        }
    }

    public boolean insertRecord(String endpointId, long deviceId, NCRoutingTableRecord record) {
        boolean recordWasUpdated = false;

        if (!table.containsKey(deviceId)) {
            record.distance += 1;
            record.nextHopEndpointId = endpointId;
            record.updateTime();
            table.put(deviceId, record);
            recordWasUpdated = true;
        }

        return recordWasUpdated;
    }

    public boolean updateRecord(String endpointId, long deviceId, NCRoutingTableRecord record) {
        boolean recordWasUpdated = false;

        if (table.containsKey(deviceId)) {
            NCRoutingTableRecord stale = table.get(deviceId);
            if (stale.distance > record.distance + 1) {
                stale.distance = record.distance + 1;
                stale.nextHopEndpointId = endpointId;
            }
            stale.username = record.username;
            stale.updateTime();
            stale.room = new Room(record.room.deviceId, record.room.name);
            table.put(deviceId, stale);
            recordWasUpdated = true;
        }

        return recordWasUpdated;
    }

    public List<String> getNeighborEndpoints() {
        List<String> result = new ArrayList<>();
        for (NCRoutingTableRecord record : table.values()) {
            if (record.isNeighbor()) {
                result.add(record.nextHopEndpointId);
            }
        }
        return result;
    }

    // we return a copy of each room so that the table is not
    // altered by users.
    public Set<Room> getRooms() {
        Set<Room> rooms = new ArraySet<>();
        for (NCRoutingTableRecord record : table.values()) {
            if (record.room.isValidRoom()) {
                Room r = new Room();
                r.name = record.room.name;
                r.deviceId = record.room.deviceId;
                rooms.add(r);
            }
        }
        return rooms;
    }

    public Profile getProfile(long deviceId) {
        NCRoutingTableRecord r = table.get(deviceId);
        Profile p = new Profile();
        p.deviceId = deviceId;
        p.username = new String(r.username);
        return p;
    }

    public NCRoutingTableRecord put(long deviceId, NCRoutingTableRecord record) {
        return table.put(deviceId, record);
    }

    public NCRoutingTableRecord get(long deviceId) {
        return table.get(deviceId);
    }

    public int size() {
        return table.size();
    }

    @NonNull
    @Override
    public Iterator<Long> iterator() {
        return table.keySet().iterator();
    }
}
