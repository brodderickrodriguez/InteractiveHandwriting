package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.routing;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;
import group6.interactivehandwriting.common.network.nearby.connections.device.NCRoutingTable;
import group6.interactivehandwriting.common.network.nearby.connections.device.NCRoutingTableRecord;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 10/28/18.
 */

public class RoutingUpdate implements SerialMessageData<RoutingUpdate> {
    private static final int BYTE_SIZE = 1000;

    NCRoutingTable table;

    public RoutingUpdate withTable(NCRoutingTable table) {
        this.table = table;
        return this;
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.ROUTING_UPDATE_REPLY;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        int recordCount = table.size();
        buffer.putInt(recordCount);

        for (Long deviceId : table) {
            buffer.putLong(deviceId);
            addRecord(buffer, table.get(deviceId));
        }

        return buffer.array();
    }

    public void addRecord(ByteBuffer buffer, NCRoutingTableRecord record) {
        buffer.putLong(record.timeStamp);
        buffer.putInt(record.distance);
        NCNetworkUtility.putNetworkString(buffer, record.username);
        Log.v(NCNetworkUtility.DEBUG, "string to bytes: " + record.username);

        NCNetworkUtility.putNetworkString(buffer, record.nextHopEndpointId);
        buffer.putLong(record.room.deviceId);
        NCNetworkUtility.putNetworkString(buffer, record.room.name);
    }

    @Override
    public RoutingUpdate fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        table = new NCRoutingTable();

        int recordCount = buffer.getInt();
        for (int i = 0; i < recordCount; i++) {
            long deviceId = buffer.getLong();
            NCRoutingTableRecord record = getRecord(buffer);
            table.put(deviceId, record);
        }

        return this;
    }

    public NCRoutingTableRecord getRecord(ByteBuffer buffer) {
        NCRoutingTableRecord record = new NCRoutingTableRecord();

        record.timeStamp = buffer.getLong();
        Log.v(NCNetworkUtility.DEBUG, "timestamp: " + record.timeStamp);
        record.distance = buffer.getInt();
        record.username = NCNetworkUtility.getNetworkString(buffer);
        Log.v(NCNetworkUtility.DEBUG, "username found: " + record.username);
        record.nextHopEndpointId = NCNetworkUtility.getNetworkString(buffer);
        record.room.deviceId = buffer.getLong();
        record.room.name = NCNetworkUtility.getNetworkString(buffer);


        return record;
    }

    @Override
    public int getByteBufferSize() {
        return BYTE_SIZE;
    }

    @Override
    public NCRoutingTable getData() {
        return table;
    }
}
