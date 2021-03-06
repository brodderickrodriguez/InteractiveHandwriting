package group6.interactivehandwriting.common.app.actions.draw;

import android.graphics.drawable.Drawable;
import android.util.Log;

import group6.interactivehandwriting.common.app.TimeStamp;
import group6.interactivehandwriting.common.app.actions.Action;
import group6.interactivehandwriting.common.app.actions.ActionId;
import group6.interactivehandwriting.common.app.actions.ActionType;
import group6.interactivehandwriting.activities.Room.draw.drawables.Line;

/**
 * Created by JakeL on 9/29/18.
 */

public class StartDrawAction extends DrawableAction {
    private float xPosition;
    private float yPosition;
    private float penWidth;
    private int rColor;
    private int gColor;
    private int bColor;
    private int alphaColor;
    private boolean isErase;

    public StartDrawAction(boolean shouldGetNewActionId) {
        setActionIdIfNeeded(shouldGetNewActionId);
        timeStamp = new TimeStamp();
        isErase = false;
    }

    private void setActionIdIfNeeded(boolean shouldGetNewActionId) {
        if (shouldGetNewActionId) {
            id = ActionId.next();
        } else {
            id = ActionId.get();
        }
    }

    @Override
    public Drawable update(Drawable drawableItem) {
        Line line = new Line();
        line.startDraw(this);
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

    public void setColor(int r, int g, int b, int a) {
        rColor = r;
        gColor = g;
        bColor = b;
        alphaColor = a;
    }

    public void setErase(boolean shouldErase) {
        isErase = shouldErase;
    }

    public boolean isEraser() {
        return isErase;
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
