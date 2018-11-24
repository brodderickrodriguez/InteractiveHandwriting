package group6.interactivehandwriting.activities.Room.draw;

import android.graphics.drawable.Drawable;

import java.util.List;

import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.TimeStamp;
import group6.interactivehandwriting.common.app.actions.Action;
import group6.interactivehandwriting.common.app.actions.ActionId;

/**
 * Created by JakeL on 11/9/18.
 */

public class DrawableRecord {
    public Profile profile;
    public Drawable drawable;
    public TimeStamp creationTime;
    public int sourceId;
    public int maxSequenceNumber;
    List<Action> actionHistory;
}
