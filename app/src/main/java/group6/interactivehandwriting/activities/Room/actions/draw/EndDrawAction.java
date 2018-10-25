package group6.interactivehandwriting.activities.Room.actions.draw;

/**
 * Created by JakeL on 9/30/18.
 */

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
    public ActionType getType() {
        return ActionType.END_DRAW;
    }
}
