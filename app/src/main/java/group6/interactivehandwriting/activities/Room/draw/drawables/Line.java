package group6.interactivehandwriting.activities.Room.draw.drawables;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import group6.interactivehandwriting.common.app.actions.draw.EndDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.StartDrawAction;

/**
 * Created by JakeL on 10/1/18.
 */

public class Line extends DrawableCanvasItem {
    Paint paint;
    Path path;

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        // TODO
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        // TODO
    }

    @Override
    public int getOpacity() {
        // TODO
        return PixelFormat.OPAQUE;
    }

    public void startDraw(StartDrawAction action) {
        paint = new Paint();
        if (action.isEraser()) {
            setColor(action.getAlpha(), 255, 255, 255);
        } else {
            setColor(action.getAlpha(), action.getRed(), action.getGreen(), action.getBlue());
        }

        paint.setStrokeWidth(action.getWidth());
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAlpha(255);

        path = new Path();
        path.moveTo(action.getX(), action.getY());
    }

    private void setColor(int alpha, int r, int g, int b) {
        paint.setARGB(alpha, r, g, b);
    }

    private void setPen(float width) {

    }

    public void moveDraw(MoveDrawAction action) {
        float x1 = action.getX();
        float y1 = action.getY();
        float x2 = x1 + action.getOffsetX();
        float y2 = y1 + action.getOffsetY();

        path.quadTo(x1, y1, x2, y2);
    }

    public void endDraw(EndDrawAction action) {
        path.lineTo(action.getX(), action.getY());
    }
}
