package group6.interactivehandwriting.common.network.nearby.connections;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessage;

/**
 * Created by JakeL on 9/22/18.
 */

public class NCNetworkUtility {
    public static final int BYTES_IN_A_FLOAT = 4;
    public static final int BYTES_IN_A_INT = 4;

    // TODO i'm pretty sure there is a better way to do this - Jake
    public static String getPayloadTypeName(int enumeratedPayloadType) {
        // Payload enumeration documentation:
        //      https://developers.google.com/android/reference/com/google/android/gms/nearby/connection/Payload.Type
        switch(enumeratedPayloadType) {
            case 1:
                return "BYTES";
            case 2:
                return "FILE";
            case 3:
                return "STREAM";
            default:
                return "UNKNOWN";
        }
    }
}
