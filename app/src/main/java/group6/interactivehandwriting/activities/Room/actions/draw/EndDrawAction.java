package group6.interactivehandwriting.activities.Room.actions.draw;

/**
 * Created by JakeL on 9/30/18.
 */

import android.graphics.drawable.Drawable;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.activities.Room.draw.drawables.Line;
import group6.interactivehandwriting.activities.Room.actions.Action;
import group6.interactivehandwriting.common.network.NetworkMessageType;
import group6.interactivehandwriting.common.network.NetworkUtility;

/**
 * Created by JakeL on 9/30/18.
 */

public class EndDrawAction extends DrawAction {
    private float xPosition;
    private float yPosition;

    public EndDrawAction() {
        setActionId(Action.getCurrentActionId());
    }

    @Override
    public Drawable update(Drawable drawableItem) {
        // assert item is correct type
        Line line = (Line) drawableItem;
        line.endAction(this);
        return line;
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

        return buffer.array();
    }

    @Override
    public void unpack(byte[] messageData) {
        ByteBuffer buffer = ByteBuffer.wrap(messageData);

        actionId = buffer.getInt();
        xPosition = buffer.getFloat();
        yPosition = buffer.getFloat();
    }

    @Override
    public int getByteBufferSize() {
        int actionIdSize = NetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NetworkUtility.BYTES_IN_A_FLOAT;

        int size = 0;
        size += actionIdSize + xPositionSize + yPositionSize;

        return size;
    }
}
