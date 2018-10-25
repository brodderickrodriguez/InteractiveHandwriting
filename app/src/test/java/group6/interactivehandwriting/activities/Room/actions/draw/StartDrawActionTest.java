package group6.interactivehandwriting.activities.Room.actions.draw;

/**
 * Created by JakeL on 10/1/18.
 */

import org.junit.Test;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;

import static org.junit.Assert.*;

public class StartDrawActionTest {
    @Test
    public void test_actionId() {
        StartDrawAction action = new StartDrawAction(false);
        int initialId = action.getActionId();
        int nextId = initialId + 1;
        action = new StartDrawAction(true);
        assertEquals(nextId, action.getActionId());
        action = new StartDrawAction(false);
        assertEquals(nextId, action.getActionId());
    }

    @Test
    public void test_getType() {
        StartDrawAction action = new StartDrawAction(false);
        assertEquals(NetworkMessageType.START_DRAW, action.getType());
    }

    @Test
    public void test_setPosition() {
        float x = 12.1f;
        float y = 10.5f;

        StartDrawAction action = new StartDrawAction(false);
        action.setPosition(x, y);
        assertEquals(x, action.getX(), 0);
        assertEquals(y, action.getY(), 0);
    }

    @Test
    public void test_setColor() {
        int r = 0;
        int g = 255;
        int b = 0;
        int a = 255;

        StartDrawAction action = new StartDrawAction(false);
        action.setColor(r, g, b, a);
        assertEquals(r, action.getRed());
        assertEquals(g, action.getGreen());
        assertEquals(b, action.getBlue());
        assertEquals(a, action.getAlpha());
    }

    @Test
    public void test_setWidth() {
        StartDrawAction action = new StartDrawAction(false);
        action.setWidth(10);
        assertEquals(10, action.getWidth(), 0);
    }

    @Test
    public void test_pack_unpack() {
        float x = 12.2f;
        float y = 10.1f;
        int r = 0, g = 255, b = 0, a = 255;
        float width = 10.6f;

        StartDrawAction action = new StartDrawAction(true);
        action.setPosition(x, y);
        action.setColor(r, g, b, a);
        action.setWidth(width);
        byte[] packedBytes = action.pack();

        StartDrawAction unpackedAction = new StartDrawAction(true);
        assertNotEquals(action.getActionId(), unpackedAction.getActionId());

        unpackedAction.unpack(packedBytes);
        assertEquals(action.getActionId(), unpackedAction.getActionId());
        assertEquals(action.getX(), unpackedAction.getX(), 0);
        assertEquals(action.getY(), unpackedAction.getY(), 0);
        assertEquals(r, action.getRed());
        assertEquals(g, action.getGreen());
        assertEquals(b, action.getBlue());
        assertEquals(a, action.getAlpha());
        assertEquals(action.getWidth(), unpackedAction.getWidth(), 0);
    }

    @Test
    public void test_getByteBufferSize() {
        StartDrawAction action = new StartDrawAction(false);
        assertEquals(32, action.getByteBufferSize());
    }
}
