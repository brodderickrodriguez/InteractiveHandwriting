package group6.interactivehandwriting.activities.Room.actions;

public abstract class Action {
    private static int actionIdCount;

    protected ActionType type;
    protected int actionId;

    static {
        Action.actionIdCount = 0;
    }

    public static int getCurrentActionId() {
        return Action.actionIdCount;
    }

    public abstract ActionType getType();

    public void setActionId(int id) {
        actionId = id;
    };

    public int getActionId() {
        return actionId;
    }

    protected static int getNextActionId() {
        Action.actionIdCount += 1;
        return Action.actionIdCount;
    }
}
