package group6.interactivehandwriting.common.actions;

public abstract class Action {
    private int actionId;

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

    protected void setActionId(int id) {
        actionId = id;
    };

    public int getActionId() {
        return actionId;
    }
}
