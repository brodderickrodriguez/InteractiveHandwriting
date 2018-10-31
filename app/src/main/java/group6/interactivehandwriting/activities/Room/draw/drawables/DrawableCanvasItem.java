package group6.interactivehandwriting.activities.Room.draw.drawables;

import android.graphics.drawable.Drawable;

import group6.interactivehandwriting.common.app.actions.ActionId;

/**
 * Created by JakeL on 10/1/18.
 */

public abstract class DrawableCanvasItem extends Drawable {
    public long deviceId;
    public String username;
    public ActionId actionId;
}
