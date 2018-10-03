package group6.interactivehandwriting.activities.Room.actions.draw;

/**
 * Created by JakeL on 10/1/18.
 */

import org.junit.Test;

import group6.interactivehandwriting.common.network.NetworkMessageType;

import static org.junit.Assert.*;

public class MoveDrawActionTest {
    @Test
    public void test_actionId() {
        StartDrawAction startAction = new StartDrawAction(true);
        MoveDrawAction moveAction = new MoveDrawAction();
        assertEquals(startAction.getActionId(), moveAction.getActionId());
    }

    @Test
    public void test_getType() {
        MoveDrawAction action = new MoveDrawAction();
        assertEquals(NetworkMessageType.MOVE_DRAW, action.getType());
    }

    @Test
    public void test_setMovePosition() {
        float x = 1.23f;
        float y = 2.34f;
        float dx = 9.89f;
        float dy = 2.34f;

        MoveDrawAction action = new MoveDrawAction();
        action.setMovePosition(x, y, dx, dy);

        assertEquals(x, action.getX(), 0);
        assertEquals(y, action.getY(), 0);
        assertEquals(dx, action.getOffsetX(), 0);
        assertEquals(dy, action.getOffsetY(), 0);
    }

    @Test
    public void test_pack_unpack() {
        float x = 1.23f;
        float y = 2.34f;
        float dx = 9.89f;
        float dy = 2.34f;

        MoveDrawAction first = new MoveDrawAction();
        first.setMovePosition(x, y, dx, dy);
        byte[] packedBytes = first.pack();

        MoveDrawAction second = new MoveDrawAction();
        second.unpack(packedBytes);

        assertEquals(first.getActionId(), second.getActionId());
        assertEquals(x, second.getX(), 0);
        assertEquals(y, second.getY(), 0);
        assertEquals(dx, second.getOffsetX(), 0);
        assertEquals(dy, second.getOffsetY(), 0);
    }

    @Test
    public void test_getByteBufferSize() {
        MoveDrawAction action = new MoveDrawAction();
        assertEquals(20, action.getByteBufferSize());
    }
}
