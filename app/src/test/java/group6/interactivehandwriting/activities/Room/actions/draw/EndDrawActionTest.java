package group6.interactivehandwriting.activities.Room.actions.draw;

/**
 * Created by JakeL on 10/1/18.
 */

import org.junit.Test;

import group6.interactivehandwriting.common.network.NetworkMessageType;

import static org.junit.Assert.*;

public class EndDrawActionTest {
    @Test
    public void test_actionId() {
        StartDrawAction startAction = new StartDrawAction(true);
        MoveDrawAction moveAction = new MoveDrawAction();
        EndDrawAction endAction = new EndDrawAction();
        assertEquals(startAction.getActionId(), moveAction.getActionId());
    }

    @Test
    public void test_getType() {
        EndDrawAction action = new EndDrawAction();
        assertEquals(NetworkMessageType.END_DRAW, action.getType());
    }

    @Test
    public void test_setMovePosition() {
        float x = 1.23f;
        float y = 2.34f;

        EndDrawAction action = new EndDrawAction();
        action.setPosition(x, y);

        assertEquals(x, action.getX(), 0);
        assertEquals(y, action.getY(), 0);
    }

    @Test
    public void test_pack_unpack() {
        float x = 1.23f;
        float y = 2.34f;

        EndDrawAction first = new EndDrawAction();
        first.setPosition(x, y);
        byte[] packedBytes = first.pack();

        EndDrawAction second = new EndDrawAction();
        second.unpack(packedBytes);

        assertEquals(first.getActionId(), second.getActionId());
        assertEquals(x, second.getX(), 0);
        assertEquals(y, second.getY(), 0);
    }

    @Test
    public void test_getByteBufferSize() {
        MoveDrawAction action = new MoveDrawAction();
        assertEquals(20, action.getByteBufferSize());
    }

    // TODO exceptions with unpacking
}
