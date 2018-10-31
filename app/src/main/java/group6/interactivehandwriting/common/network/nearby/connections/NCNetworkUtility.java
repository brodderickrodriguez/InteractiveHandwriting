package group6.interactivehandwriting.common.network.nearby.connections;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessage;

/**
 * Created by JakeL on 9/22/18.
 */

public class NCNetworkUtility {
    public static final String DEBUG = "Network";

    public static final int BYTES_IN_A_FLOAT = 4;
    public static final int BYTES_IN_A_INT = 4;
    public static final int BYTES_IN_A_LONG = 8;

    public static final Charset CHAR_SET = Charset.forName("UTF-8");

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

    public static void putNetworkString(ByteBuffer buffer, String str) {
        buffer.putInt(str.length());
        byte[] bytes = str.getBytes(CHAR_SET);
        for (byte b : bytes) {
            buffer.put(b);
        }
    }

    public static String getNetworkString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes, 0, length);
        return new String(bytes, CHAR_SET);
    }

    public static int getNetworkStringSize(String str) {
        int size = 0;
        size += BYTES_IN_A_INT; // for the size of the string
        size += str.getBytes(CHAR_SET).length;
        return size;
    }
}
