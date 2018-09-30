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
}
