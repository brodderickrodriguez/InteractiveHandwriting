package group6.interactivehandwriting.activities.Room.actions.draw;

import android.graphics.drawable.Drawable;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.activities.Room.actions.ActionType;
import group6.interactivehandwriting.activities.Room.draw.drawables.Line;
import group6.interactivehandwriting.activities.Room.actions.Action;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;

/**
 * Created by JakeL on 9/30/18.
 */

public class MoveDrawAction extends DrawAction {
    private float xPosition;
    private float yPosition;
    private float dX;
    private float dY;

    public MoveDrawAction() {
        setActionId(Action.getCurrentActionId());
    }

    @Override
    public ActionType getType() {
        return ActionType.MOVE_DRAW;
    }

    @Override
    public Drawable update(Drawable drawableItem) {
        // assert item is correct type
        Line line = (Line) drawableItem;
        line.moveAction(this);
        return line;
    }

    public void setMovePosition(float x, float y, float offsetX, float offsetY) {
        xPosition = x;
        yPosition = y;
        dX = offsetX;
        dY = offsetY;
    }

    public float getX() {
        return xPosition;
    }

    public float getY() {
        return yPosition;
    }

    public float getOffsetX() {
        return dX;
    }

    public float getOffsetY() {
        return dY;
    }
}
