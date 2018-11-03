package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import group6.interactivehandwriting.activities.Room.actions.draw.DrawAction;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkManager;

public class RoomView extends View {
    private static final String DEBUG_TAG_V = "RoomView";

    private static final float TOUCH_TOLERANCE = 4;

    private NetworkManager networkManager;

    private CanvasManager canvasManager;
    private String deviceName;

    public RoomView(Context context, Profile profile, NetworkManager networkManager) {
        super(context);
        this.networkManager = networkManager;
        deviceName = profile.getDeviceName();
        canvasManager = new CanvasManager(this);
        networkManager.setCanvasManager(canvasManager);
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

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void touchStarted(float x, float y) {
        DrawAction action = RoomViewActionUtility.touchStarted(x, y);
        handleDrawAction(action);
    }

    private void touchMoved(float x, float y) {
        if (RoomViewActionUtility.didTouchMove(x, y, TOUCH_TOLERANCE)) {
            DrawAction action = RoomViewActionUtility.touchMoved(x, y);
            handleDrawAction(action);
        }
    }

    private void touchReleased() {
        DrawAction action = RoomViewActionUtility.touchReleased();
        handleDrawAction(action);
    }

    private void handleDrawAction(DrawAction action) {
        canvasManager.putAction(deviceName, action);
        networkManager.sendMessage(action);
    }
}
