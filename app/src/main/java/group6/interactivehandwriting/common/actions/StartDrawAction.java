package group6.interactivehandwriting.common.actions;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.exceptions.UnpackException;
import group6.interactivehandwriting.common.network.NetworkMessage;
import group6.interactivehandwriting.common.network.NetworkUtility;

/**
 * Created by JakeL on 9/29/18.
 */

public class StartDrawAction extends UserAction implements NetworkMessage<StartDrawAction> {
    private int actionId;
    private float positionX;
    private float positionY;

    public StartDrawAction(float x, float y) {
        positionX = x;
        positionY = y;

        setActionId();
    }

    public int getByteBufferSize() {
        int actionIdSize = NetworkUtility.BYTES_IN_A_INT;
        int actionTypeSize = NetworkUtility.BYTES_IN_A_INT;
        int positionXSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int positionYSize = NetworkUtility.BYTES_IN_A_FLOAT;

        int size = actionIdSize + actionTypeSize + positionXSize + positionYSize;

        return size;
    }

    @Override
    protected void setActionId() {
        actionId = UserAction.getNextActionId();
    }

    @Override
    public int getActionId() {
        return actionId;
    }

    @Override
    public UserActionType getActionType() {
        return UserActionType.START_DRAW;
    }

    @Override
    public byte[] pack() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putInt(actionId);
        buffer.putInt(getActionType().getValue());
        buffer.putFloat(positionX);
        buffer.putFloat(positionY);

        return buffer.array();
    }

    @Override
    public StartDrawAction unpack(byte[] payload) throws UnpackException {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        actionId = buffer.getInt();
        int actionTypeValue = buffer.getInt();
        positionX = buffer.getFloat();
        positionY = buffer.getFloat();

        if (actionTypeValue != getActionType().getValue()) {
            throw new UnpackException();
        }

        return this;
    }
}
