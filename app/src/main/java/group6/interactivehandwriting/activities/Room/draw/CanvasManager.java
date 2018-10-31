package group6.interactivehandwriting.activities.Room.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

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
    private Map<Profile, DrawableManager> indexedDrawables;

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

    @Override
    public void handleDrawAction(Profile user, DrawableAction action) {
        Log.v(DEBUG, "Profile with device id " + user.deviceId);
        Log.v(DEBUG, "Profile with username " + user.username);


        DrawableManager drawables = indexedDrawables.get(user);
        if (drawables == null) {
            Log.v(DEBUG, "drawables was null");
            drawables = new DrawableManager();
            indexedDrawables.put(user, drawables);
        }

        Log.v(DEBUG, "drawables size: " + drawables.size());
        Log.v(DEBUG, "action with id: " + action.getId());
        Drawable canvasItem = drawables.get(action.getId());
        Log.v(DEBUG, "canvas item is not null? " + (canvasItem != null));
        canvasItem = action.update(canvasItem);
        Log.v(DEBUG, "canvas item is not null? " + (canvasItem != null));
        ActionId id = action.getId();
        drawables.put(id, canvasItem);

        parentView.invalidate();
    }

    public void update(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasBitmapPaint);

        for (DrawableManager drawables : indexedDrawables.values()) {
            for (Drawable item : drawables.values()) {
                item.draw(canvas);
            }
        }
    }
}
