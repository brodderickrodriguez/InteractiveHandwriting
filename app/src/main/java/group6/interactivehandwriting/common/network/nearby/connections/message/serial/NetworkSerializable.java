package group6.interactivehandwriting.common.network.nearby.connections.message.serial;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessage;

/**
 * Created by JakeL on 10/19/18.
 */

public interface NetworkSerializable<Self> extends NetworkMessage {
    public byte[] toBytes();

    public Self fromBytes(byte[] bytes);

    public int getByteBufferSize();
}
