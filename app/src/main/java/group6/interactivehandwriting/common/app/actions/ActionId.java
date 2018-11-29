package group6.interactivehandwriting.common.app.actions;

import android.support.v4.app.ActivityCompat;

import group6.interactivehandwriting.common.app.rooms.Room;

/**
 * Created by JakeL on 10/30/18
 */

public class ActionId {
    private static int lastValue;
    private static int sequenceNumber;

    public int id;
    public int sequence;

    public ActionId(int id, int sequence) {
        this.id = id;
        this.sequence = sequence;
    }

    static {
        lastValue = 0;
        sequenceNumber = 0;
    }

    public static ActionId get() {
        sequenceNumber++;
        return new ActionId(lastValue, sequenceNumber);
    }

    public static ActionId next() {
        lastValue++;
        sequenceNumber = 0;
        return new ActionId(lastValue, sequenceNumber);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (other instanceof ActionId) {
            return this.id == ((ActionId) other).id;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ActionId with id " + id + " and sequence " + sequence;
    }
}
