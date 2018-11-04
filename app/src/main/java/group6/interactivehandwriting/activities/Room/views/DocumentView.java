package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.actions.draw.EndDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.StartDrawAction;
import group6.interactivehandwriting.common.network.NetworkLayer;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class DocumentView extends ImageView {
    private static final String DEBUG_TAG_V = "DocumentView";

    private static final float TOUCH_TOLERANCE = 4;

    private CanvasManager canvasManager;
    private Profile profile;
    private String deviceName;

    private NetworkLayer networkLayer;

    private Drawable mImage;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mScaleFactor = 1.0f;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;

    private ConstraintLayout.LayoutParams layoutParams;

    // 0 - zoom/resize .. 1 - drawing
    private int allowDrawing = 1;

    /*
     * Need to add button to finish the activity and button to switch between allowing to draw and allowing to resize
     */

    public DocumentView(Context context, Profile profile, NetworkLayer networkManager) {
        super(context);
        this.networkLayer = networkLayer;
        this.profile = profile;
        canvasManager = new CanvasManager(this);
        //networkLayer.receiveDrawActions(canvasManager);

        mScaleDetector = new ScaleGestureDetector(context, new DocumentScaleListener());
    }

    public DocumentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new DocumentScaleListener());
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
        switch (allowDrawing) {
            case 0: /* Resize / Zoom */
                mScaleDetector.onTouchEvent(event);

                final int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        final float x = event.getX();
                        final float y = event.getY();

                        mLastTouchX = x;
                        mLastTouchY = y;
                        mActivePointerId = event.getPointerId(0);
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        final int pointerIndex = event.findPointerIndex(mActivePointerId);
                        final float x = event.getX(pointerIndex);
                        final float y = event.getY(pointerIndex);

                        // Only move if the ScaleGestureDetector isn't processing a gesture.
                        if (!mScaleDetector.isInProgress()) {
                            final float dx = x - mLastTouchX;
                            final float dy = y - mLastTouchY;

                            mPosX += dx;
                            mPosY += dy;

                            invalidate();
                        }

                        mLastTouchX = x;
                        mLastTouchY = y;

                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_POINTER_UP: {
                        final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                        final int pointerId = event.getPointerId(pointerIndex);
                        if (pointerId == mActivePointerId) {
                            // This was our active pointer going up. Choose a new
                            // active pointer and adjust accordingly.
                            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                            mLastTouchX = event.getX(newPointerIndex);
                            mLastTouchY = event.getY(newPointerIndex);
                            mActivePointerId = event.getPointerId(newPointerIndex);
                        }
                        break;
                    }
                }

                break;
            case 1: /* Draw */
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
        networkLayer.startDraw(action);
        canvasManager.handleDrawAction(profile, action);
    }

    private void touchMoved(float x, float y) {
        if (RoomViewActionUtility.didTouchMove(x, y, TOUCH_TOLERANCE)) {
            MoveDrawAction action = RoomViewActionUtility.touchMoved(x, y);
            networkLayer.moveDraw(action);
            canvasManager.handleDrawAction(profile, action);
        }
    }

    private void touchReleased() {
        EndDrawAction action = RoomViewActionUtility.touchReleased();
        networkLayer.endDraw(action);
        canvasManager.handleDrawAction(profile, action);
    }

    public ImageView getView() {
        return this;
    }

    public int getDrawingStatus() {
        return this.allowDrawing;
    }

    public void setDrawingStatus(int allowDrawingIn) {
        this.allowDrawing = allowDrawingIn;
    }

    public class DocumentScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){

            mScaleFactor *= scaleGestureDetector.getScaleFactor();

            mScaleFactor = Math.max(0.1f,

                    Math.min(mScaleFactor, 10.0f));

            getView().setScaleX(mScaleFactor);

            getView().setScaleY(mScaleFactor);

            return true;

        }
    }
}


