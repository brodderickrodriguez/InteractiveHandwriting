package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class DocumentView extends android.support.v7.widget.AppCompatImageView {
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
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        resizeMode = ResizeMode.INACTIVE;
        xPosition = 0;
        yPosition = 0;
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            self().setScaleX(scaleFactor);
            self().setScaleY(scaleFactor);
            Log.v("RESIZE", "onScale() called");
            invalidate();
            return true;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (resizeMode == ResizeMode.ACTIVE) {
            //resizeEvent(event); TODO: Fix document resizing
        }
        return true;
    }


    private void resizeEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();

                xTouchLast = x;
                yTouchLast = y;
                pointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(pointerId);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!scaleDetector.isInProgress()) {
                    final float dx = x - xTouchLast;
                    final float dy = y - yTouchLast;

                    xPosition += dx;
                    yPosition += dy;

                    invalidate();
                }

                xTouchLast = x;
                yTouchLast = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                pointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                pointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int nextPointerId = event.getPointerId(pointerIndex);
                if (nextPointerId == pointerId) {
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

    public DocumentView self() {
        return this;
    }
}