package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkLayer;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class DocumentView extends android.support.v7.widget.AppCompatImageView {

//    private static final float MIN_SCALE = 0.95f;
//    private static final float MAX_SCALE = 2.0f;
//
//    private NetworkLayer networkLayer;

    private CanvasManager canvasManager;
//    private Profile profile;

//    private float mLastTouchX;
//    private float mLastTouchY;
//    private float mPosX;
//    private float mPosY;
//
    private float mScaleFactor = 0.5f;

    private ScaleGestureDetector mDocumentScaleDetector;
//
//    private View documentView = findViewById(R.id.documentView);

    private Bitmap pdfPages[];
    private int curPDFPage;


    private enum ResizeMode {
        ACTIVE, INACTIVE
    }
//    private ResizeMode resizeMode;

    public DocumentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        canvasManager = new CanvasManager(this);

        invalidate();
//        resizeMode = ResizeMode.INACTIVE;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        canvasManager.updateSize(w, h);
    }
//
//    private class DocumentScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            mScaleFactor *= detector.getScaleFactor();
//
//            // Don't let the object get too small or too large.
//            if (mScaleFactor > MAX_SCALE) {
//                mScaleFactor = MAX_SCALE;
//            }
//            if (mScaleFactor < MIN_SCALE) {
//                mScaleFactor = MIN_SCALE;
//            }
//
//            self().setScaleX(mScaleFactor);
//            self().setScaleY(mScaleFactor);
//
//            invalidate();
//            return true;
//        }
//    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvasManager.update(canvas);
        canvas.restore();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (resizeMode == ResizeMode.ACTIVE) {
//            resizeEvent(event);
//        }
//        return true;
//    }
//
//
//    private void resizeEvent(MotionEvent event) {
//
//        mDocumentScaleDetector.onTouchEvent(event);
//
//        final int action = event.getAction();
//
//
//        switch(action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN: {
//
//                final float x = (event.getX() - scalePointX)/mScaleFactor;
//                final float y = (event.getY() - scalePointY)/mScaleFactor;
//                mLastTouchX = x;
//                mLastTouchY = y;
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//
//                final float x = (event.getX() - scalePointX)/mScaleFactor;
//                final float y = (event.getY() - scalePointY)/mScaleFactor;
//                // Only move if the ScaleGestureDetector isn't processing a gesture.
//                if (!mDocumentScaleDetector.isInProgress()) {
//                    final float dx = x - mLastTouchX; // change in X
//                    final float dy = y - mLastTouchY; // change in Y
//                    mPosX += dx;
//                    mPosY += dy;
//                    invalidate();
//
//                    self().animate()
//                            .x(mPosX * mScaleFactor)
//                            .y(mPosY * mScaleFactor)
//                            .setDuration(0)
//                            .start();
//
//                }
//
//                mLastTouchX = x;
//                mLastTouchY = y;
//                break;
//
//            }
//            case MotionEvent.ACTION_UP: {
//                final float x = (event.getX() - scalePointX)/mScaleFactor;
//                final float y = (event.getY() - scalePointY)/mScaleFactor;
//                mLastTouchX = 0;
//                mLastTouchY = 0;
//                invalidate();
//            }
//        }
//    }

    public void setPDF(Bitmap bitmapArr[]) {
        this.pdfPages = bitmapArr;
        this.curPDFPage = 0;
        setImageBitmap(bitmapArr[0]);
    }

    public void incPDFPage() {
        int pageCount = this.pdfPages.length;
        if (pageCount == 0) {
            return;
        }
        if (this.curPDFPage == pageCount - 1) {
            this.curPDFPage = 0;
        }
        else {
            this.curPDFPage++;
        }
        setImageBitmap(this.pdfPages[this.curPDFPage]);
    }

    public void decPDFPage() {
        int pageCount = this.pdfPages.length;
        if (pageCount == 0) {
            return;
        }
        if (this.curPDFPage == 0) {
            this.curPDFPage = pageCount - 1;
        }
        else {
            this.curPDFPage--;
        }
        setImageBitmap(this.pdfPages[this.curPDFPage]);
    }
//
//    public void activateResizeMode() {
//        resizeMode = ResizeMode.ACTIVE;
//    }
//
//    public void deactivateResizeMode() {
//        resizeMode = ResizeMode.INACTIVE;
//    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public DocumentView self() {
        return this;
    }

}
