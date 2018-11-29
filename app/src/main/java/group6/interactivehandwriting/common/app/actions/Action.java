package group6.interactivehandwriting.common.app.actions;

import group6.interactivehandwriting.common.app.TimeStamp;

public abstract class Action {
    public ActionId id;
    public TimeStamp timeStamp;

    public abstract ActionType getType();
}
