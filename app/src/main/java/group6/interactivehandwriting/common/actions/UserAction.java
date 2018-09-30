package group6.interactivehandwriting.common.actions;

import group6.interactivehandwriting.common.actions.states.ActionState;

public abstract class UserAction {
    private static int actionIdCount;

    static {
        actionIdCount = 0;
    }

    protected static int getNextActionId() {
        actionIdCount += 1;
        return actionIdCount;
    }

    protected static int getCurrentActionId() {
        return actionIdCount;
    }

    protected abstract void setActionId();

    public abstract int getActionId();

    public abstract UserActionType getActionType();
}
