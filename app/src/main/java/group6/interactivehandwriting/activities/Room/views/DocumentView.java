package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class DocumentView extends android.support.v7.widget.AppCompatImageView {
    private static final float TOUCH_TOLERANCE = 4;

    private float xPosition;
    private float yPosition;

    private float xTouchLast;
    private float yTouchLast;
    private float scaleFactor = 1.0f;
    private int pointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector scaleDetector;

    private enum ResizeMode {
        ACTIVE, INACTIVE;
    }
    private ResizeMode resizeMode;

    public DocumentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        scaleDetector = new ScaleGestureDetector(context, new DocumentScaleListener());
        resizeMode = ResizeMode.INACTIVE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (resizeMode == ResizeMode.ACTIVE) {
            resizeEvent(event);
        }
        return true;
    }


    private void resizeEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        final int action = event.getAction();
        int pointerIndex = 0;
        float x = 0;
        float y = 0;
        float dx = 0;
        float dy = 0;
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                xTouchLast = x;
                yTouchLast = y;
                pointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                pointerIndex = event.findPointerIndex(pointerId);
                x = event.getX(pointerIndex);
                y = event.getY(pointerIndex);
                if (scaleDetector.isInProgress()) {
                    dx = x - xTouchLast;
                    dy = y - yTouchLast;
                    xPosition += dx;
                    yPosition += dy;
                    invalidate();
                }
                xTouchLast = x;
                yTouchLast = y;
                break;
            case MotionEvent.ACTION_UP:
                pointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
                pointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                pointerId = event.getPointerId(pointerIndex);
                if (pointerId == pointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    xTouchLast = event.getX(newPointerIndex);
                    yTouchLast = event.getY(newPointerIndex);
                    pointerId = event.getPointerId(newPointerIndex);
                }
                break;
        }
    }

    public DocumentView(Context context) {
        super(context);
    }

    public void activateResizeMode() {
        resizeMode = ResizeMode.ACTIVE;
    }

    public void deactivateResizeMode() {
        resizeMode = ResizeMode.INACTIVE;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public class DocumentScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
            return true;
        }
    }
}