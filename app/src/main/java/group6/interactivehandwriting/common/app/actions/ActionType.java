package group6.interactivehandwriting.common.app.actions;

/**
 * Created by JakeL on 10/18/18.
 */

public enum ActionType {
    START_DRAW(1),
    MOVE_DRAW(2),
    END_DRAW(3);

    int value;

    ActionType(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    public static ActionType fromValue(int v) {
        switch(v) {
            case 1:
                return START_DRAW;
            case 2:
                return MOVE_DRAW;
            case 3:
                return END_DRAW;
            default:
                return null;
        }
    }
}
