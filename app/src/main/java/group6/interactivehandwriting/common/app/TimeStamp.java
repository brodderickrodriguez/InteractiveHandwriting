package group6.interactivehandwriting.common.app;

/**
 * Created by JakeL on 11/23/18.
 */

public class TimeStamp {
    private long time;

    public TimeStamp() {
        update();
    }

    public TimeStamp(long ms) {
        setMilliseconds(ms);
    }

    public void update() {
        time = System.currentTimeMillis();
    }

    public void setMilliseconds(long ms) {
        time = ms;
    }

    public long milliseconds() {
        return time;
    }
}
