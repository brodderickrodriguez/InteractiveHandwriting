package group6.interactivehandwriting.activities.Room.actions.draw;

import android.graphics.drawable.Drawable;

import group6.interactivehandwriting.activities.Room.actions.Action;
import group6.interactivehandwriting.activities.Room.actions.ActionType;
import group6.interactivehandwriting.activities.Room.draw.drawables.Line;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.SerialMessage;

/**
 * Created by JakeL on 9/29/18.
 */

public class StartDrawAction extends DrawAction {
    private float xPosition;
    private float yPosition;
    private float penWidth;
    private int rColor;
    private int gColor;
    private int bColor;
    private int alphaColor;

    public StartDrawAction(boolean shouldGetNewActionId) {
        setActionIdIfNeeded(shouldGetNewActionId);
    }

    @Override
    public Drawable update(Drawable drawableItem) {
        // assert item is correct type
        Line line = new Line();
        line.startAction(this);
        return line;
    }

    private void setActionIdIfNeeded(boolean shouldGetNewActionId) {
        int id;
        if (shouldGetNewActionId) {
            id = Action.getNextActionId();
        } else {
            id = Action.getCurrentActionId();
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
    public ActionType getType() {
        return ActionType.START_DRAW;
    }
}
