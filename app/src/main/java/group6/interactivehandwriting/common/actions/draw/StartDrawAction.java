package group6.interactivehandwriting.common.actions.draw;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.actions.NetworkedByteAction;
import group6.interactivehandwriting.common.network.NetworkMessageType;
import group6.interactivehandwriting.common.network.NetworkUtility;

/**
 * Created by JakeL on 9/29/18.
 */

public class StartDrawAction extends NetworkedByteAction {
    private int actionId;
    private float xPosition;
    private float yPosition;
    private float penWidth;
    private float rColor;
    private float gColor;
    private float bColor;
    private float alphaColor;


    StartDrawAction(boolean shouldGetNewActionId) {
        if (shouldGetNewActionId) {
            setActionId(NetworkedByteAction.getNextActionId());
        } else {
            setActionId(NetworkedByteAction.getCurrentActionId());
        }
    }

    public void setPosition(float x, float y) {
        xPosition = x;
        yPosition = y;
    }

    public void setColor(float r, float g, float b, float a) {
        rColor = r;
        gColor = g;
        bColor = b;
        alphaColor = a;
    }

    public void setWidth(float width) {
        penWidth = width;
    }


    //////////////////////////////////////////////////////
    // Action
    //////////////////////////////////////////////////////

    @Override
    public int getActionId() {
        return actionId;
    }

    //////////////////////////////////////////////////////
    // NetworkMessage interface
    //////////////////////////////////////////////////////

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.START_DRAW;
    }

    @Override
    public byte[] pack() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putInt(actionId);
        buffer.putFloat(xPosition);
        buffer.putFloat(yPosition);
        buffer.putFloat(penWidth);
        buffer.putFloat(rColor);
        buffer.putFloat(gColor);
        buffer.putFloat(bColor);
        buffer.putFloat(alphaColor);

        return buffer.array();
    }

    // TODO add exceptions
    @Override
    public void unpack(byte[] messageData) {
        ByteBuffer buffer = ByteBuffer.wrap(messageData);

        actionId = buffer.getInt();
        xPosition = buffer.getFloat();
        yPosition = buffer.getFloat();
        penWidth = buffer.getFloat();
        rColor = buffer.getFloat();
        gColor = buffer.getFloat();
        bColor = buffer.getFloat();
        alphaColor = buffer.getFloat();
    }

    public int getByteBufferSize() {
        int actionIdSize = NetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int penWidthSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int rColorSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int gColorSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int bColorSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int alphaColorSize = NetworkUtility.BYTES_IN_A_FLOAT;

        int size = 0;
        size += actionIdSize + xPositionSize + yPositionSize + penWidthSize;
        size += rColorSize + gColorSize + bColorSize + alphaColorSize;

        return size;
    }
}
