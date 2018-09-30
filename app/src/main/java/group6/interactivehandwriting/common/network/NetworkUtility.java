package group6.interactivehandwriting.common.network;

/**
 * Created by JakeL on 9/22/18.
 */

public class NetworkUtility {
    public static final int BYTES_IN_A_FLOAT = 4;
    public static final int BYTES_IN_A_INT = 4;

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
