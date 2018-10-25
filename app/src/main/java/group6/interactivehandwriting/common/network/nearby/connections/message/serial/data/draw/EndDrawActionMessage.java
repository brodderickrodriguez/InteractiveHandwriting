package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.activities.Room.actions.draw.EndDrawAction;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 10/19/18.
 */

public class EndDrawActionMessage implements SerialMessageData<EndDrawActionMessage> {
    EndDrawAction action;

    public static EndDrawActionMessage fromAction(EndDrawAction action) {
        EndDrawActionMessage self = new EndDrawActionMessage();
        self.withAction(action);
        return self;
    }

    public static EndDrawAction actionFromBytes(byte[] data) {
        EndDrawActionMessage self = new EndDrawActionMessage();
        EndDrawAction action = self.fromBytes(data).getData();
        return action;
    }

    public EndDrawActionMessage withAction(EndDrawAction action) {
        this.action = action;
        return this;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteBufferSize());

        buffer.putInt(action.getActionId());
        buffer.putFloat(action.getX());
        buffer.putFloat(action.getY());

        return buffer.array();
    }

    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.END_DRAW;
    }

    @Override
    public EndDrawActionMessage fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        int actionId = buffer.getInt();
        float xPosition = buffer.getFloat();
        float yPosition = buffer.getFloat();

        action = new EndDrawAction();
        action.setActionId(actionId);
        action.setPosition(xPosition, yPosition);

        return this;
    }

    @Override
    public int getByteBufferSize() {
        int actionIdSize = NCNetworkUtility.BYTES_IN_A_INT;
        int xPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;
        int yPositionSize = NCNetworkUtility.BYTES_IN_A_FLOAT;

        int size = 0;
        size += actionIdSize + xPositionSize + yPositionSize;

        return size;
    }

    @Override
    public EndDrawAction getData() {
        return action;
    }
}
