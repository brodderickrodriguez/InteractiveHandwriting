package group6.interactivehandwriting.activities.Room.actions.draw;

import android.graphics.drawable.Drawable;

import group6.interactivehandwriting.activities.Room.actions.Action;
import group6.interactivehandwriting.activities.Room.actions.ActionType;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;

/**
 * Created by JakeL on 10/1/18.
 */

public abstract class DrawAction extends Action {
    public abstract Drawable update(Drawable drawableItem);

    public static DrawAction getDrawAction(ActionType type) {
        switch (type) {
            case START_DRAW:
                return new StartDrawAction(false);
            case MOVE_DRAW:
                return new MoveDrawAction();
            case END_DRAW:
                return new EndDrawAction();
            default:
                return null;
        }
    }
}
