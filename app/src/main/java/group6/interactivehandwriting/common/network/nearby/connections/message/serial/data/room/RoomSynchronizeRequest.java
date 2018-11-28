package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.room;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 11/24/18.
 */

public class RoomSynchronizeRequest implements SerialMessageData<RoomSynchronizeRequest> {
    int roomNumber;

    public RoomSynchronizeRequest withRoomNumber(int number) {
        roomNumber = number;
        return this;
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.SYNC_REQUEST;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putInt(roomNumber);

        return buffer.array();
    }

    @Override
    public RoomSynchronizeRequest fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        roomNumber = buffer.getInt();

        return this;
    }

    @Override
    public int getByteBufferSize() {
        return NCNetworkUtility.BYTES_IN_A_INT;
    }

    @Override
    public Integer getData() {
        return roomNumber;
    }
}
