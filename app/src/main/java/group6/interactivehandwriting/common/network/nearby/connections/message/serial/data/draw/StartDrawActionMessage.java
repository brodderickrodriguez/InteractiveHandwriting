package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.app.TimeStamp;
import group6.interactivehandwriting.common.app.actions.ActionId;
import group6.interactivehandwriting.common.app.actions.draw.StartDrawAction;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
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

        buffer.putLong(action.timeStamp.milliseconds());
        buffer.putInt(action.id.id);
        buffer.putInt(action.id.sequence);
        buffer.putFloat(action.getX());
        buffer.putFloat(action.getY());
        buffer.putFloat(action.getWidth());
        buffer.putInt(action.getRed());
        buffer.putInt(action.getGreen());
        buffer.putInt(action.getBlue());
        buffer.putInt(action.getAlpha());
        buffer.putInt(action.isEraser() ? 1 : 0);

        return buffer.array();
    }

    @Override
    public StartDrawActionMessage fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        long timeMs = buffer.getLong();
        int actionIdValue = buffer.getInt();
        int actionIdSeq = buffer.getInt();
        float xPosition = buffer.getFloat();
        float yPosition = buffer.getFloat();
        float penWidth = buffer.getFloat();
        int rColor = buffer.getInt();
        int gColor = buffer.getInt();
        int bColor = buffer.getInt();
        int alphaColor = buffer.getInt();
        boolean isErase = buffer.getInt() == 1 ? true : false;

        action = new StartDrawAction(false);
        action.timeStamp = new TimeStamp(timeMs);
        action.id = new ActionId(actionIdValue, actionIdSeq);
        action.setPosition(xPosition, yPosition);
        action.setWidth(penWidth);
        action.setColor(rColor, gColor, bColor, alphaColor);
        action.setErase(isErase);

        return this;
    }

    @Override
    public int getByteBufferSize() {
        int timeStampSize = NCNetworkUtility.BYTES_IN_A_LONG;
        int actionIdSize = 2 * NCNetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int penWidthSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int rColorSize = NCNetworkUtility.BYTES_IN_A_INT;
        int gColorSize = NCNetworkUtility.BYTES_IN_A_INT;
        int bColorSize = NCNetworkUtility.BYTES_IN_A_INT;
        int alphaColorSize = NCNetworkUtility.BYTES_IN_A_INT;
        int eraseSize = NCNetworkUtility.BYTES_IN_A_INT;

        int size = 0;
        size += timeStampSize;
        size += actionIdSize + xPositionSize + yPositionSize + penWidthSize;
        size += rColorSize + gColorSize + bColorSize + alphaColorSize + eraseSize;

        return size;
    }

    @Override
    public StartDrawAction getData() {
        return action;
    }
}
