package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import group6.interactivehandwriting.activities.Room.actions.draw.DrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.EndDrawAction;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;

public class RoomView extends View {
    private static final String DEBUG_TAG_V = "RoomView";

    private static final float TOUCH_TOLERANCE = 4;

    private CanvasManager canvasManager;
    private float touchX, touchY;
    private String deviceName;

    public RoomView(Context context) {
        super(context);
        deviceName = getDeviceId();
        canvasManager = new CanvasManager(this);
    }

    private String getDeviceId() {
        return "Scribe" + (new Random()).nextInt(100);
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
    }
}
