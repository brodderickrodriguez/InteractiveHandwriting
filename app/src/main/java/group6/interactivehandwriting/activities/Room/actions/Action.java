package group6.interactivehandwriting.activities.Room.actions;

public abstract class Action {
    private static int actionIdCount;

    protected int actionId;

    static {
        Action.actionIdCount = 0;
    }

    protected static int getNextActionId() {
        Action.actionIdCount += 1;
        return Action.actionIdCount;
    }

    protected static int getCurrentActionId() {
        return Action.actionIdCount;
    }

    protected void setActionId(int id) {
        actionId = id;
    };

    public int getActionId() {
        return actionId;
    }
}
