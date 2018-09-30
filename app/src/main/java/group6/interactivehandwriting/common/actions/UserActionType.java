package group6.interactivehandwriting.common.actions;

public enum UserActionType {
    START_DRAW(1),
    MOVE_DRAW(2),
    END_DRAW(3);

    private int value;

    UserActionType(int n) {
        value = n;
    }

    public int getValue() {
        return value;
    }
}
