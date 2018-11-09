package group6.interactivehandwriting.activities.Room.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import group6.interactivehandwriting.activities.Room.draw.drawables.DrawableManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.actions.ActionId;
import group6.interactivehandwriting.common.app.actions.draw.DrawableAction;
import group6.interactivehandwriting.common.app.actions.DrawActionHandle;

/**
 * Created by JakeL on 9/29/18.
 */

public class CanvasManager implements DrawActionHandle {
    public static final String DEBUG = "CanvasManager";
    private LinkedList<DrawableRecord> items;

    private View parentView;

    private Bitmap canvasBitmap;
    private Paint canvasBitmapPaint;

    public CanvasManager(View parent) {
        this.parentView = parent;
        canvasBitmapPaint = new Paint(Paint.DITHER_FLAG);
        canvasBitmapPaint.setARGB(255, 255, 255, 255);
        items = new LinkedList<>();
    }

    public void updateSize(int w, int h) {
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
    }

    @Override
    public void handleDrawAction(Profile user, DrawableAction action) {
        Log.v(DEBUG, "Profile with device id " + user.deviceId);
        Log.v(DEBUG, "Profile with username " + user.username);

        updateDrawable(user, action);
        parentView.invalidate();
    }

    private boolean updateDrawable(Profile user, DrawableAction action) {
        int index = 0;

        boolean found = false;
        ListIterator<DrawableRecord> recordIterator = items.listIterator(0);
        while(!found && recordIterator.hasNext()) {
            DrawableRecord record = recordIterator.next();
            if (record.profile.equals(user) && action.getId().equals(record.actionId)) {
                found = true;
            }
            index++;
        }
        index--;

        if (found) {
            DrawableRecord record = items.remove(index);
            record.drawable = action.update(record.drawable);
            items.addLast(record);
        } else {
            DrawableRecord record = new DrawableRecord();
            record.profile = user;
            record.actionId = action.getId();
            record.drawable = action.update(null);
            items.addLast(record);
        }

        return found;
    }

    public void update(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasBitmapPaint);
        canvas.drawARGB(255, 255, 255, 255);
        for (DrawableRecord record : items) {
            record.drawable.draw(canvas);
        }
    }

    public void undo(Profile user) {
        boolean removed = false;
        ListIterator<DrawableRecord> iterator = items.listIterator(items.size());
        while (!removed && iterator.hasPrevious()) {
            DrawableRecord record = iterator.previous();
            if (record.profile.equals(user)) {
                iterator.remove();
                removed = true;
            }
        }
        parentView.invalidate();
    }
}
