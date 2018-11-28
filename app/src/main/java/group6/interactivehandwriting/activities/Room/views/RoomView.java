package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Color;
import group6.interactivehandwriting.R;

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

    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;
    private float cX, cY; // circle coords

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    private float scalePointX;
    private float scalePointY;

    private Paint marker;

    // 0 - zoom/resize .. 1 - drawing
    private int allowDrawing = 1;

    public RoomView(Context context) {
        super(context);
        canvasManager = new CanvasManager(this);

        mScaleDetector = new ScaleGestureDetector(context, new RoomScaleListener());

        marker = new Paint();
        marker.setColor(Color.RED);
        marker.setAlpha(125);
        marker.setTextSize(20);
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

        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, scalePointX, scalePointY);
        canvas.translate(mPosX, mPosY);
        canvas.drawCircle(cX, cY, 10, marker);

        canvasManager.update(canvas);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (allowDrawing) {
            case 0:
                mScaleDetector.onTouchEvent(ev);

                final int action = ev.getAction();


                switch(action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {

                        final float x = (ev.getX() - scalePointX)/mScaleFactor;
                        final float y = (ev.getY() - scalePointY)/mScaleFactor;
                        cX = x - mPosX + scalePointX; // canvas X
                        cY = y - mPosY + scalePointY; // canvas Y
                        mLastTouchX = x;
                        mLastTouchY = y;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {

                        final float x = (ev.getX() - scalePointX)/mScaleFactor;
                        final float y = (ev.getY() - scalePointY)/mScaleFactor;
                        cX = x - mPosX + scalePointX; // canvas X
                        cY = y - mPosY + scalePointY; // canvas Y
                        // Only move if the ScaleGestureDetector isn't processing a gesture.
                        if (!mScaleDetector.isInProgress()) {
                            final float dx = x - mLastTouchX; // change in X
                            final float dy = y - mLastTouchY; // change in Y
                            mPosX += dx;
                            mPosY += dy;
                            invalidate();
                        }

                        mLastTouchX = x;
                        mLastTouchY = y;
                        break;

                    }
                    case MotionEvent.ACTION_UP: {
                        final float x = (ev.getX() - scalePointX)/mScaleFactor;
                        final float y = (ev.getY() - scalePointY)/mScaleFactor;
                        cX = x - mPosX + scalePointX; // canvas X
                        cY = y - mPosY + scalePointY; // canvas Y
                        mLastTouchX = 0;
                        mLastTouchY = 0;
                        invalidate();
                    }
                }
                return true;
            case 1: /* Draw */
                performClick();
                float x = ev.getX();
                float y = ev.getY();
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchStarted(x - mPosX + scalePointX, y - mPosY + scalePointY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchMoved(x - mPosX + scalePointX, y - mPosY + scalePointY);
                        break;
                    case MotionEvent.ACTION_UP:
                        touchReleased();
                        break;
                }
                break;
        }
        return true;
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

    public View getView() {
        return this;
    }

    public int getDrawingStatus() {
        return this.allowDrawing;
    }

    public void setDrawingStatus(int allowDrawingIn) {
        this.allowDrawing = allowDrawingIn;
    }

    private class RoomScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            scalePointX =  detector.getFocusX();
            scalePointY = detector.getFocusY();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 2.0f));

            invalidate();
            return true;
        }
    }
}
