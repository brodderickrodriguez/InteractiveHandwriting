package group6.interactivehandwriting.common.network;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by JakeL on 9/22/18.
 */

public class NetworkUtility {
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

    // TODO maybe some of these methods should move to a NetworkBytes class of some kind (header, data)
    // TODO unit test the below functions
    public static byte[] getDataSection(byte[] bytes, int headerLength) {
        int length = bytes.length - headerLength;
        byte[] result = new byte[length];
        for (int i = headerLength; i < bytes.length; i++) {
            result[i - headerLength] = bytes[i];
        }
        return result;
    }

    public static byte[] createByteMessage(NetworkMessage<byte[]> message, int headerLength) {
        byte[] header = createHeader(message, headerLength);
        byte[] data = message.pack();
        return joinByteArrays(header, data);
    }

    public static byte[] createHeader(NetworkMessage<byte[]> message, int headerLength) {
        ByteBuffer buffer = ByteBuffer.allocate(headerLength);
        buffer.putInt(message.getType().getValue());
        return buffer.array();
    }

    public static byte[] joinByteArrays(byte[] one, byte[] two) {
        int length = one.length + two.length;
        byte[] result = new byte[length];

        for (int i = 0; i < one.length; i++) {
            result[i] = one[i];
        }
        for (int i = one.length; i < length; i++) {
            result[i] = two[i - one.length];
        }

        return result;
    }

}
