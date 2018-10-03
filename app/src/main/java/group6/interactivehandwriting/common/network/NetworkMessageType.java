package group6.interactivehandwriting.common.network;

/**
 * Created by JakeL on 9/30/18.
 */

public enum NetworkMessageType {
    START_DRAW(1),
    MOVE_DRAW(2),
    END_DRAW(3);

    private int value;

    NetworkMessageType(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    public static NetworkMessageType fromValue(int v) {
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
