package group6.interactivehandwriting.common.network;

import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.common.exceptions.InvalidPayloadException;

/**
 * Created by JakeL on 9/19/18.
 */

public interface NetworkMessage {
    public Payload pack();
    public NetworkMessage unpack(Payload payload) throws InvalidPayloadException;
}
