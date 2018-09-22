package group6.interactivehandwriting.common.exceptions;

/**
 * Created by JakeL on 9/22/18.
 */

import com.google.android.gms.nearby.connection.Payload;

import org.junit.Test;
import static org.junit.Assert.*;

public class InvalidPayloadExceptionTest {
    @Test
    public void test_invalidPayloadExceptionMessage() {
        String receiverObject = new String();
        Payload payload = Payload.fromBytes(new byte[0]);
        InvalidPayloadException payloadException = new InvalidPayloadException(receiverObject, payload);

        String expectedMessage = "Payload receiver \'java.lang.String\' could not resolve payload with type \'BYTES\'";
        String actualMessage = payloadException.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
