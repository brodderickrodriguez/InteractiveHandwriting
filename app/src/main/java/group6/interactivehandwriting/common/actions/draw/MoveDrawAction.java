package group6.interactivehandwriting.common.actions.draw;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.actions.Action;
import group6.interactivehandwriting.common.actions.NetworkedByteAction;
import group6.interactivehandwriting.common.network.NetworkMessageType;
import group6.interactivehandwriting.common.network.NetworkUtility;

/**
 * Created by JakeL on 9/30/18.
 */

public class MoveDrawAction extends NetworkedByteAction {
    private int actionId;
    private float xPosition;
    private float yPosition;
    private float dX;
    private float dY;

    public MoveDrawAction() {
        setActionId(Action.getCurrentActionId());
    }

    public void setMovePosition(float x, float y, float offsetX, float offsetY) {
        xPosition = x;
        yPosition = y;
        dX = offsetX;
        dY = offsetY;
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.END_DRAW;
    }

    @Override
    public byte[] pack() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putInt(actionId);
        buffer.putFloat(xPosition);
        buffer.putFloat(yPosition);
        buffer.putFloat(dX);
        buffer.putFloat(dY);

        return buffer.array();
    }

    @Override
    public void unpack(byte[] messageData) {
        ByteBuffer buffer = ByteBuffer.wrap(messageData);

        actionId = buffer.getInt();
        xPosition = buffer.getFloat();
        yPosition = buffer.getFloat();
        dX = buffer.getFloat();
        dY = buffer.getFloat();
    }

    @Override
    public int getByteBufferSize() {
        int actionIdSize = NetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int dXSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int dYSize = NetworkUtility.BYTES_IN_A_FLOAT;

        int size = 0;
        size += actionIdSize + xPositionSize + yPositionSize + dXSize + dYSize;

        return size;
    }
}
