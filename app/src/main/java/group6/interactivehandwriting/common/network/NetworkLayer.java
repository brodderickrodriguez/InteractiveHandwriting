package group6.interactivehandwriting.common.network;

import group6.interactivehandwriting.activities.Room.actions.draw.DrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.EndDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.StartDrawAction;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;

/**
 * Created by JakeL on 9/22/18.
 */

public interface NetworkLayer<T> {
    public void begin(Profile profile);

    public void setCanvasManager(CanvasManager canvasManager);

    public void startDraw(StartDrawAction action);
    public void moveDraw(MoveDrawAction action);
    public void endDraw(EndDrawAction action);
}
