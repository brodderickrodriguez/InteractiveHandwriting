package group6.interactivehandwriting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.Random;
import java.util.jar.Attributes;


// https://stackoverflow.com/questions/16650419/draw-in-canvas-by-finger-android


public class DrawingView extends View {

    private Context context;
    private Canvas canvas;

    private Bitmap mBitmap;
    private Paint mBitmapPaint;

    private Path circlePath;
    private Paint circlePaint;

    private Path linePath;
    private Paint linePaint;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public DrawingView(Context c) {
        this(c, null);
    }

    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.context = context;
        linePath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePath = new Path();

        createLinePaintObject();
        createCirclePaintObject();
    }

    public void setDrawingColor(int color) {
        circlePaint.setColor(color);
        linePaint.setColor(color);
    }

    private void createLinePaintObject() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(12);
    } // createPaintObject()


    private void createCirclePaintObject() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4);
    } // createCirclePaintObject()


    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(mBitmap);
    } // onSizeChanged()


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(linePath, linePaint);
        canvas.drawPath(circlePath, circlePaint);
    } // onDraw()


    private void touchStarted(float x, float y) {
        linePath.reset();
        linePath.moveTo(x, y);
        mX = x;
        mY = y;
    } // touchStarted()


    private void touchMoved(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            linePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    } // touchMoved()


    private void touchReleased() {
        linePath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        canvas.drawPath(linePath, linePaint);
        // kill this so we don't double draw
        linePath.reset();
    } // touchReleased()


    @Override
    public boolean performClick() {
        return super.performClick();
    } // performClick()


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();

        float x = event.getX();
        float y = event.getY();

        Toast.makeText(context, "TOUCH AT X: "+ x +" Y: " + y, Toast.LENGTH_SHORT).show();

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
        } // switch

        invalidate();
        return true;
    } // onTouchEvent()


} // DrawingView
