package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.app.TimeStamp;
import group6.interactivehandwriting.common.app.actions.ActionId;
import group6.interactivehandwriting.common.app.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 10/19/18.
 */

public class MoveDrawActionMessage implements SerialMessageData<MoveDrawActionMessage> {
    MoveDrawAction action;

    public static MoveDrawActionMessage fromAction(MoveDrawAction action) {
        MoveDrawActionMessage self = new MoveDrawActionMessage();
        return self.withAction(action);
    }

    public static MoveDrawAction actionFromBytes(byte[] data) {
        MoveDrawActionMessage self = new MoveDrawActionMessage();
        MoveDrawAction action = self.fromBytes(data).getData();
        return action;
    }

    public MoveDrawActionMessage withAction(MoveDrawAction action) {
        this.action = action;
        return this;
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.MOVE_DRAW;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putInt(action.id.id);
        buffer.putInt(action.id.sequence);
        buffer.putFloat(action.getX());
        buffer.putFloat(action.getY());
        buffer.putFloat(action.getOffsetX());
        buffer.putFloat(action.getOffsetY());

        return buffer.array();
    }

    @Override
    public MoveDrawActionMessage fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        int actionIdValue = buffer.getInt();
        int actionIdSeq = buffer.getInt();
        float xPosition = buffer.getFloat();
        float yPosition = buffer.getFloat();
        float dX = buffer.getFloat();
        float dY = buffer.getFloat();

        action = new MoveDrawAction();
        action.id = new ActionId(actionIdValue, actionIdSeq);
        action.setMovePosition(xPosition, yPosition, dX, dY);

        return this;
    }

    @Override
    public int getByteBufferSize() {
        int actionIdSize = 2 * NCNetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int dXSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int dYSize = NCNetworkUtility.BYTES_IN_A_FLOAT;

        int size = 0;
        size += actionIdSize + xPositionSize + yPositionSize + dXSize + dYSize;

        return size;
    }

    @Override
    public MoveDrawAction getData() {
        return action;
    }
}
