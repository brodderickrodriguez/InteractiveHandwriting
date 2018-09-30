package group6.interactivehandwriting.common.network;

import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.common.exceptions.InvalidPayloadException;
import group6.interactivehandwriting.common.execptions.UnpackException;

/**
 * Created by JakeL on 9/19/18.
 */

public interface NetworkMessage<T> {
    public byte[] pack();

    public T unpack(byte[] payload) throws UnpackException;

    public int byteArraySize();
}

