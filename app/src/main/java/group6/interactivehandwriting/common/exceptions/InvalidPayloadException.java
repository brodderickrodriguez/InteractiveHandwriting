package group6.interactivehandwriting.common.exceptions;

import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;

/**
 * Created by JakeL on 9/22/18.
 */

public final class InvalidPayloadException extends Exception {
    private String message;

    public InvalidPayloadException(Object payloadReceiver, Payload receivedPayload) {
        super();
        setMessage(payloadReceiver, receivedPayload);
    }

    private void setMessage(Object payloadReceiver, Payload receivedPayload) {
        String receiverName = payloadReceiver.getClass().getName();
        String payloadType = NCNetworkUtility.getPayloadTypeName(receivedPayload.getType());

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder
                .append("Payload receiver \'")
                .append(receiverName)
                .append("\' could not resolve payload with type \'")
                .append(payloadType)
                .append("\'");

        message = messageBuilder.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
