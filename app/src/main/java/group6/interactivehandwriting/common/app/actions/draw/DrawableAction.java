package group6.interactivehandwriting.common.app.actions.draw;

import android.graphics.drawable.Drawable;

import group6.interactivehandwriting.common.app.actions.Action;

/**
 * Created by JakeL on 10/1/18.
 */

public abstract class DrawableAction extends Action {
    public abstract Drawable update(Drawable drawableItem);
}
