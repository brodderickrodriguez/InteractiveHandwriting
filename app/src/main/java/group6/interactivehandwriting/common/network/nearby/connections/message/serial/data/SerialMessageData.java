package group6.interactivehandwriting.common.network.nearby.connections.message.serial.data;

import group6.interactivehandwriting.common.network.nearby.connections.message.serial.NetworkSerializable;

/**
 * Created by JakeL on 10/21/18.
 */

public interface SerialMessageData<Self> extends NetworkSerializable<Self> {
    public Object getData();
}
