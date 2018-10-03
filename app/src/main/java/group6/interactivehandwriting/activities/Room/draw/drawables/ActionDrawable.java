package group6.interactivehandwriting.activities.Room.draw.drawables;

import android.graphics.drawable.Drawable;

import group6.interactivehandwriting.activities.Room.actions.Action;

/**
 * Created by JakeL on 10/1/18.
 */

public abstract class ActionDrawable extends Drawable {
    protected int actionId;

    public int getActionId() {
        return actionId;
    }

    protected void setActionId(Action action) {
        actionId = action.getActionId();
    }

    protected boolean isSequentialAction(Action action) {
        return actionId == action.getActionId();
    }
}
