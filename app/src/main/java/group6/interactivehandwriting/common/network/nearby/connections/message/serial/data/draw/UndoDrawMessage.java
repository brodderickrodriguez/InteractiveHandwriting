package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw;


import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 11/24/18.
 */

public class UndoDrawMessage implements SerialMessageData<UndoDrawMessage> {
    Profile profile;

    public UndoDrawMessage withProfile(Profile p) {
        profile = p;
        return this;
    }

    public static UndoDrawMessage undoFromBytes(byte[] bytes) {
        UndoDrawMessage message = new UndoDrawMessage();
        message = message.fromBytes(bytes);
        return message;
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.UNDO_DRAW;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putLong(profile.deviceId);

        return buffer.array();
    }

    @Override
    public UndoDrawMessage fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        long id = buffer.getLong();

        profile = new Profile();
        profile.deviceId = id;

        return this;
    }

    @Override
    public int getByteBufferSize() {
        return NCNetworkUtility.BYTES_IN_A_LONG;
    }

    @Override
    public Profile getData() {
        return profile;
    }
}
