package group6.interactivehandwriting.activities.Room.actions.draw;

import android.graphics.drawable.Drawable;

import group6.interactivehandwriting.activities.Room.actions.NetworkedByteAction;
import group6.interactivehandwriting.common.network.NetworkMessageType;

/**
 * Created by JakeL on 10/1/18.
 */

public abstract class DrawAction extends NetworkedByteAction {
    public abstract NetworkMessageType getType();

    public abstract Drawable update(Drawable drawableItem);

    public static DrawAction getDrawAction(NetworkMessageType messageType) {
        if (messageType == NetworkMessageType.START_DRAW) {
            return new StartDrawAction(false);
        } else if (messageType == NetworkMessageType.MOVE_DRAW) {
            return new MoveDrawAction();
        } else if (messageType == NetworkMessageType.END_DRAW) {
            return new EndDrawAction();
        } else {
            return null;
        }
    }
}
