package group6.interactivehandwriting.common.actions.draw;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.actions.NetworkedByteAction;
import group6.interactivehandwriting.common.network.NetworkMessageType;
import group6.interactivehandwriting.common.network.NetworkUtility;

/**
 * Created by JakeL on 9/29/18.
 */

public class StartDrawAction extends NetworkedByteAction {
    private float xPosition;
    private float yPosition;
    private float penWidth;
    private int rColor;
    private int gColor;
    private int bColor;
    private int alphaColor;


    StartDrawAction(boolean shouldGetNewActionId) {
        setActionIdIfNeeded(shouldGetNewActionId);
    }

    private void setActionIdIfNeeded(boolean shouldGetNewActionId) {
        int id;
        if (shouldGetNewActionId) {
            id = NetworkedByteAction.getNextActionId();
        } else {
            id = NetworkedByteAction.getCurrentActionId();
        }
        setActionId(id);
    }

    public void setPosition(float x, float y) {
        xPosition = x;
        yPosition = y;
    }

    public float getX() {
        return xPosition;
    }

    public float getY() {
        return yPosition;
    }

    public void setColor(int r, int g, int b, int a) {
        rColor = r;
        gColor = g;
        bColor = b;
        alphaColor = a;
    }

    public int getRed() {
        return rColor;
    }

    public int getGreen() {
        return gColor;
    }

    public int getBlue() {
        return bColor;
    }

    public int getAlpha() {
        return alphaColor;
    }

    public void setWidth(float width) {
        penWidth = width;
    }

    public float getWidth() {
        return penWidth;
    }

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
        buffer.putInt(rColor);
        buffer.putInt(gColor);
        buffer.putInt(bColor);
        buffer.putInt(alphaColor);

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
        rColor = buffer.getInt();
        gColor = buffer.getInt();
        bColor = buffer.getInt();
        alphaColor = buffer.getInt();
    }

    public int getByteBufferSize() {
        int actionIdSize = NetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int penWidthSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int rColorSize = NetworkUtility.BYTES_IN_A_INT;
        int gColorSize = NetworkUtility.BYTES_IN_A_INT;
        int bColorSize = NetworkUtility.BYTES_IN_A_INT;
        int alphaColorSize = NetworkUtility.BYTES_IN_A_INT;

        int size = 0;
        size += actionIdSize + xPositionSize + yPositionSize + penWidthSize;
        size += rColorSize + gColorSize + bColorSize + alphaColorSize;

        return size;
    }
}
