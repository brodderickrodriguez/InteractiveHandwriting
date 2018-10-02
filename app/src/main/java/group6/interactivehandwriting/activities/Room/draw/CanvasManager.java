package group6.interactivehandwriting.activities.Room.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import group6.interactivehandwriting.activities.Room.actions.draw.DrawAction;

/**
 * Created by JakeL on 9/29/18.
 */

public class CanvasManager {
    private Map<String, Map<Integer, Drawable>> indexedDrawables;

    private View parentView;

    private Bitmap canvasBitmap;
    private Paint canvasBitmapPaint;

    public CanvasManager(View parent) {
        this.parentView = parent;
        canvasBitmapPaint = new Paint(Paint.DITHER_FLAG);
        indexedDrawables = new HashMap<>();
    }

    public void updateSize(int w, int h) {
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
    }

    public void putAction(String deviceName, DrawAction action) {
        Map<Integer, Drawable> deviceItems = indexedDrawables.get(deviceName);

        if (deviceItems == null) {
            deviceItems = new HashMap<Integer, Drawable>();
            indexedDrawables.put(deviceName, deviceItems);
        }

        Log.v("CANVAS", "for device " + deviceName);
        Log.v("CANVAS", "adding item with type " + action.getType().toString());
        Log.v("CANVAS", "adding item with id " + action.getActionId());
        Drawable canvasItem = deviceItems.get(action.getActionId());
        Log.v("CANVAS", "canvas item is null: " + (canvasItem == null));
        deviceItems.put(action.getActionId(), action.update(canvasItem));

        parentView.invalidate();
    }

    public void update(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasBitmapPaint);

        for (Map<Integer, Drawable> deviceMap : indexedDrawables.values()) {
            for (Drawable item : deviceMap.values()) {
                item.draw(canvas);
            }
        }
    }


}
