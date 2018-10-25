package group6.interactivehandwriting.common.network.nearby.connections.message.serial;

import java.util.Arrays;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;

/**
 * Created by JakeL on 9/30/18.
 */

public class SerialMessage implements NetworkSerializable<SerialMessage> {
    private byte[] header;
    private byte[] data;

    public SerialMessage withHeader(SerialMessageHeader header) {
        this.header = header.toBytes();
        return this;
    }

    public SerialMessage withData(NetworkSerializable data) {
        this.data = data.toBytes();
        return this;
    }

    public byte[] getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public byte[] toBytes() {
        return joinByteArrays(header, data);
    }

    @Override
    public SerialMessage fromBytes(byte[] serialMessage) {
        this.header = getHeaderSection(serialMessage);
        this.data = getDataSection(serialMessage);
        return this;
    }

    private byte[] getHeaderSection(byte[] serialMessage) {
        return Arrays.copyOfRange(serialMessage, 0, SerialMessageHeader.BYTE_SIZE);
    }

    private byte[] getDataSection(byte[] serialMessage) {
        return Arrays.copyOfRange(serialMessage, SerialMessageHeader.BYTE_SIZE, serialMessage.length);
    }

    @Override
    public int getByteBufferSize() {
        if (header == null || data == null) {
            return 0;
        } else {
            return header.length + data.length;
        }
    }

    private byte[] joinByteArrays(byte[] one, byte[] two) {
        int length = one.length + two.length;
        byte[] result = new byte[length];

        for (int i = 0; i < one.length; i++) {
            result[i] = one[i];
        }
        for (int i = one.length; i < length; i++) {
            result[i] = two[i - one.length];
        }

        return result;
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.SERIAL;
    }
}
