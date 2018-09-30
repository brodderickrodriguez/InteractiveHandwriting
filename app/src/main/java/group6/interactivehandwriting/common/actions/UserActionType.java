package group6.interactivehandwriting.common.actions;

public enum UserActionType {
    START_DRAW(1),
    MOVE_DRAW(2),
    END_DRAW(3);

    private int value;

    private UserActionType(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
}
