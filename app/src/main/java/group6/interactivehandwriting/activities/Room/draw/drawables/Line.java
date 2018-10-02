package group6.interactivehandwriting.activities.Room.draw.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import group6.interactivehandwriting.activities.Room.actions.draw.EndDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.StartDrawAction;

/**
 * Created by JakeL on 10/1/18.
 */

public class Line extends ActionDrawable {
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

    public void startAction(StartDrawAction action) {
        setActionId(action);

        paint = new Paint();
        setColor(action.getAlpha(), action.getRed(), action.getGreen(), action.getBlue());
        setPen(action.getWidth());

        path = new Path();
        path.moveTo(action.getX(), action.getY());
    }

    private void setColor(int alpha, int r, int g, int b) {
        paint.setARGB(alpha, r, g, b);
    }

    private void setPen(float width) {
        // TODO perhaps some of these should be configurable
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void moveAction(MoveDrawAction action) {
        // TODO assert same action id
        float x1 = action.getX();
        float y1 = action.getY();
        float x2 = x1 + action.getOffsetX();
        float y2 = y1 + action.getOffsetY();

        path.quadTo(x1, y1, x2, y2);
    }

    public void endAction(EndDrawAction action) {
        // TODO assert same action id
        path.lineTo(action.getX(), action.getY());
    }
}
