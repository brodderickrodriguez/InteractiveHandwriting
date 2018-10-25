package group6.interactivehandwriting.common.network.nearby.connections.message.serial;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;

/**
 * Created by JakeL on 10/19/18.
 */

public class SerialMessageHeader implements NetworkSerializable<SerialMessageHeader> {
    public static final int BYTE_SIZE = 4 * 4;

    private NetworkMessageType type;
    private int roomNumber;
    private int sequenceNumber;
    private int deviceId;

    private static int globalSequenceNumber;

    static {
        globalSequenceNumber = 0;
    }

    public static int getNextSequenceNumber() {
        globalSequenceNumber += 1;
        return globalSequenceNumber;
    }

    public SerialMessageHeader withType(NetworkMessageType type) {
        this.type = type;
        return this;
    }

    public SerialMessageHeader withRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public SerialMessageHeader withSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public SerialMessageHeader withDeviceName(String deviceName) {
        this.deviceId = deviceName.hashCode();
        return this;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(BYTE_SIZE);
        buffer.putInt(type.getValue());
        buffer.putInt(roomNumber);
        buffer.putInt(sequenceNumber);
        buffer.putInt(deviceId);
        return buffer.array();
    }

    @Override
    public SerialMessageHeader fromBytes(byte[] header) {
        ByteBuffer buffer = ByteBuffer.wrap(header);
        type = NetworkMessageType.get(buffer.getInt());
        roomNumber = buffer.getInt();
        sequenceNumber = buffer.getInt();
        deviceId = buffer.getInt();
        return this;
    }

    @Override
    public int getByteBufferSize() {
        return BYTE_SIZE;
    }

    @Override
    public NetworkMessageType getType() {
        return type;
    }

    public int getDeviceId() {
        return deviceId;
    }
}
