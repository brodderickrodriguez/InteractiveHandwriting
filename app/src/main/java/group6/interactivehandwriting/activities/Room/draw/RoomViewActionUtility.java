package group6.interactivehandwriting.activities.Room.draw;

import java.util.Random;

import group6.interactivehandwriting.activities.Room.actions.draw.DrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.EndDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.StartDrawAction;

/**
 * Created by JakeL on 10/1/18.
 */

public class RoomViewActionUtility {
    private static float touchX;
    private static float touchY;

    private static int R;
    private static int G;
    private static int B;

    static {
        R = randomColorValue();
        G = randomColorValue();
        B = randomColorValue();
    }

    public static void setTouchPosition(float x, float y) {
        touchX = x;
        touchY = y;
    }

    public static StartDrawAction touchStarted(float x, float y) {
        setTouchPosition(x, y);
        StartDrawAction startAction = new StartDrawAction(true);
        startAction.setPosition(x, y);
        startAction.setColor(R, G, B, 255);
        startAction.setWidth(12.0f);
        return startAction;
    }

    // TODO this is wrong
    private static int randomColorValue() {
        return (new Random()).nextInt(255);
    }

    public static boolean didTouchMove(float x, float y, float tolerance) {
        float dx = x - touchX;
        float dy = y - touchY;
        return Math.abs(dx) >= tolerance || Math.abs(dy) >= tolerance;
    }

    public static MoveDrawAction touchMoved(float x, float y) {
        float dx = x - touchX;
        float dy = y - touchY;
        MoveDrawAction action = new MoveDrawAction();
        action.setMovePosition(touchX, touchY, dx, dy);
        setTouchPosition(x, y);
        return action;
    }

    public static EndDrawAction touchReleased() {
        EndDrawAction action = new EndDrawAction();
        action.setPosition(touchX, touchY);
        return action;
    }
}
