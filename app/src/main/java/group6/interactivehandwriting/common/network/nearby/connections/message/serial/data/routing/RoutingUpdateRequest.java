package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.routing;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;

/**
 * Created by JakeL on 10/28/18.
 */

public class RoutingUpdateRequest implements SerialMessageData<RoutingUpdateRequest> {
    @Override
    public NetworkMessageType getType() {
        return NetworkMessageType.ROUTING_UPDATE_REQUEST;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public RoutingUpdateRequest fromBytes(byte[] bytes) {
        return this;
    }

    @Override
    public int getByteBufferSize() {
        return 0;
    }

    @Override
    public Object getData() {
        return null;
    }
}
