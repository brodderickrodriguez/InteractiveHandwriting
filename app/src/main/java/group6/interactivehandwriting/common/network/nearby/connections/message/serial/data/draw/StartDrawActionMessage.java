package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.activities.Room.actions.draw.StartDrawAction;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.NetworkSerializable;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 10/19/18.
 */

public class StartDrawActionMessage implements SerialMessageData<StartDrawActionMessage> {
    StartDrawAction action;

    public static StartDrawActionMessage fromAction(StartDrawAction action) {
        StartDrawActionMessage self = new StartDrawActionMessage();
        self.withAction(action);
        return self;
    }

    public static StartDrawAction actionFromBytes(byte[] data) {
        StartDrawActionMessage self = new StartDrawActionMessage();
        StartDrawAction action = self.fromBytes(data).getData();
        return action;
    }

    public StartDrawActionMessage withAction(StartDrawAction action) {
        this.action = action;
        return this;
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.START_DRAW;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putInt(action.getActionId());
        buffer.putFloat(action.getX());
        buffer.putFloat(action.getY());
        buffer.putFloat(action.getWidth());
        buffer.putInt(action.getRed());
        buffer.putInt(action.getGreen());
        buffer.putInt(action.getBlue());
        buffer.putInt(action.getAlpha());

        return buffer.array();
    }

    @Override
    public StartDrawActionMessage fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        int actionId = buffer.getInt();
        float xPosition = buffer.getFloat();
        float yPosition = buffer.getFloat();
        float penWidth = buffer.getFloat();
        int rColor = buffer.getInt();
        int gColor = buffer.getInt();
        int bColor = buffer.getInt();
        int alphaColor = buffer.getInt();

        action = new StartDrawAction(false);
        action.setActionId(actionId);
        action.setPosition(xPosition, yPosition);
        action.setWidth(penWidth);
        action.setColor(rColor, gColor, bColor, alphaColor);

        return this;
    }

    @Override
    public int getByteBufferSize() {
        int actionIdSize = NCNetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int penWidthSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int rColorSize = NCNetworkUtility.BYTES_IN_A_INT;
        int gColorSize = NCNetworkUtility.BYTES_IN_A_INT;
        int bColorSize = NCNetworkUtility.BYTES_IN_A_INT;
        int alphaColorSize = NCNetworkUtility.BYTES_IN_A_INT;

        int size = 0;
        size += actionIdSize + xPositionSize + yPositionSize + penWidthSize;
        size += rColorSize + gColorSize + bColorSize + alphaColorSize;

        return size;
    }

    @Override
    public StartDrawAction getData() {
        return action;
    }
}
