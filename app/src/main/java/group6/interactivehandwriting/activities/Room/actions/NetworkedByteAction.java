package group6.interactivehandwriting.activities.Room.actions;

import group6.interactivehandwriting.common.network.NetworkMessage;

/**
 * Created by JakeL on 9/30/18.
 */

public abstract class NetworkedByteAction extends Action implements NetworkMessage<byte[]> {
    public abstract int getByteBufferSize();
}
