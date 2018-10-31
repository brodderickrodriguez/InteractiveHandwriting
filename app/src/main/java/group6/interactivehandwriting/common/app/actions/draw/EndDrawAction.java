package group6.interactivehandwriting.common.app.actions.draw;

/**
 * Created by JakeL on 9/30/18.
 */

import android.graphics.drawable.Drawable;

import group6.interactivehandwriting.common.app.actions.ActionId;
import group6.interactivehandwriting.common.app.actions.ActionType;
import group6.interactivehandwriting.activities.Room.draw.drawables.Line;
import group6.interactivehandwriting.common.app.actions.Action;


/**
 * Created by JakeL on 9/30/18.
 */

public class EndDrawAction extends DrawableAction {
    private float xPosition;
    private float yPosition;

    public EndDrawAction() {
        id = ActionId.get();
    }

    @Override
    public Drawable update(Drawable drawableItem) {
        // assert item is correct type
        Line line = (Line) drawableItem;
        line.endDraw(this);
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
