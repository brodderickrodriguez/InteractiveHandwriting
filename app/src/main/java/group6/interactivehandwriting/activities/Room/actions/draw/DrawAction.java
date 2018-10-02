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
}
