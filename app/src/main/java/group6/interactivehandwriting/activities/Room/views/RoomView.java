package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;

import group6.interactivehandwriting.common.app.actions.draw.EndDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.StartDrawAction;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkLayer;

public class RoomView extends View {
    private static final String DEBUG_TAG_V = "RoomView";

    private static final float TOUCH_TOLERANCE = 4;

    private NetworkLayer networkLayer;

    private CanvasManager canvasManager;
    private Profile profile;

    public RoomView(Context context) {
        super(context);
        canvasManager = new CanvasManager(this);
    }

    public boolean setNetworkLayer(NetworkLayer layer) {
        if (layer != null) {
            this.profile = layer.getMyProfile();
            this.networkLayer = layer;
            this.networkLayer.receiveDrawActions(canvasManager);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        canvasManager.updateSize(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasManager.update(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStarted(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMoved(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchReleased();
                break;
        }
        return true;
    }

    public void undo() {
        canvasManager.undo(profile);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void touchStarted(float x, float y) {
        StartDrawAction action = RoomViewActionUtility.touchStarted(x, y);
        canvasManager.handleDrawAction(profile, action);

        if (networkLayer != null) {
            networkLayer.startDraw(action);
        }
    }

    private void touchMoved(float x, float y) {
        if (RoomViewActionUtility.didTouchMove(x, y, TOUCH_TOLERANCE)) {
            MoveDrawAction action = RoomViewActionUtility.touchMoved(x, y);
            canvasManager.handleDrawAction(profile, action);

            if (networkLayer != null) {
                networkLayer.moveDraw(action);
            }
        }
    }

    private void touchReleased() {
        EndDrawAction action = RoomViewActionUtility.touchReleased();
        canvasManager.handleDrawAction(profile, action);

        if (networkLayer != null) {
            networkLayer.endDraw(action);
        }
    }
}
