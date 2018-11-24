package group6.interactivehandwriting.activities.Room.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.actions.Action;
import group6.interactivehandwriting.common.app.actions.draw.DrawableAction;
import group6.interactivehandwriting.common.app.actions.DrawActionHandle;

/**
 * Created by JakeL on 9/29/18.
 */

public class CanvasManager implements DrawActionHandle {
    public static final String DEBUG = "CanvasManager";

    private LinkedList<DrawableRecord> records;

    private View parentView;

    private Bitmap canvasBitmap;
    private Paint canvasBitmapPaint;

    public CanvasManager(View parent) {
        this.parentView = parent;

        canvasBitmapPaint = new Paint(Paint.DITHER_FLAG);
        canvasBitmapPaint.setARGB(255, 255, 255, 255);

        records = new LinkedList<>();
    }

    public void updateSize(int w, int h) {
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
    }

    @Override
    public void handleDrawAction(Profile user, DrawableAction action) {
        insertUpdateDrawable(user, action);
        parentView.invalidate(); // refreshes the view on screen
    }

    @Override
    public List<Action> getActionHistory() {
        List<Action> actionHistory = new ArrayList<>();

        for (DrawableRecord record : records) {
            actionHistory.addAll(record.actionHistory);
        }

        return actionHistory;
    }

    @Override
    public void mergeActionHistory(List<Action> actionHistory) {
        // pass for now - should already merge history / discarding duplicates
    }

    public void insertUpdateDrawable(Profile user, DrawableAction action) {
        boolean isUpdated = updateDrawable(user, action);
        if (!isUpdated) {
            insertDrawable(user, action);
        }
    }

    private boolean updateDrawable(Profile user, DrawableAction action) {
        boolean isUpdated = false;

        ListIterator<DrawableRecord> recordIterator = records.listIterator(0);
        while (!isUpdated && recordIterator.hasNext()) {
            DrawableRecord record = recordIterator.next();
            if (record.profile.equals(user) && action.id.id == record.sourceId) {
                if (record.maxSequenceNumber < action.id.sequence) {
                    record.maxSequenceNumber = action.id.sequence;
                    action.update(record.drawable);
                    record.actionHistory.add(action);
                }
                isUpdated = true;
            }
        }

        return isUpdated;
    }

    private void insertDrawable(Profile user, DrawableAction action) {
        DrawableRecord record = new DrawableRecord();
        record.profile = user;
        record.sourceId = action.id.id;
        record.maxSequenceNumber = action.id.sequence;
        record.creationTime = action.timeStamp;
        record.drawable = action.update(null);
        record.actionHistory = new ArrayList<>();
        record.actionHistory.add(action);

        // insert by time stamp
        int index = 0;

        ListIterator<DrawableRecord> iterator = records.listIterator(0);
        while (iterator.hasNext() && iterator.next().creationTime.milliseconds() < record.creationTime.milliseconds()) {
            index++;
        }

        records.add(index, record);
    }

    public void update(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasBitmapPaint);
        canvas.drawARGB(255, 255, 255, 255);
        for (DrawableRecord record : records) {
            record.drawable.draw(canvas);
        }
    }

    public void undo(Profile user) {
        boolean removed = false;
        ListIterator<DrawableRecord> iterator = records.listIterator(records.size());

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
