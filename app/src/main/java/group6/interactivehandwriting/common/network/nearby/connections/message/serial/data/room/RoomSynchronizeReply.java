package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.room;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 11/24/18.
 */

public class RoomSynchronizeReply implements SerialMessageData<RoomSynchronizeReply> {
    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.SYNC_REPLY;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public RoomSynchronizeReply fromBytes(byte[] bytes) {
        return null;
    }

    @Override
    public int getByteBufferSize() {
        return 0;
    }

    @Override
    public Object getData() {
        return null;
    }
}
