package group6.interactivehandwriting.common.network;

import group6.interactivehandwriting.common.actions.UserAction;
import group6.interactivehandwriting.common.exceptions.InvalidPayloadException;
import group6.interactivehandwriting.common.exceptions.UnpackException;

/**
 * Created by JakeL on 9/19/18.
 */

public interface NetworkMessage<T> {
    public NetworkMessageType getType();

    public T pack();

    public void unpack(T message);
}

