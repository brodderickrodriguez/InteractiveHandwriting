package group6.interactivehandwriting.common.network;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by JakeL on 9/22/18.
 */

public class NetworkUtilityTest {
    @Test
    public void test_getPayloadTypeName() {
        String expectedName;
        String actualName;

        expectedName = "BYTES";
        actualName = NetworkUtility.getPayloadTypeName(1);
        assertEquals(expectedName, actualName);

        expectedName = "FILE";
        actualName = NetworkUtility.getPayloadTypeName(2);
        assertEquals(expectedName, actualName);

        expectedName = "STREAM";
        actualName = NetworkUtility.getPayloadTypeName(3);
        assertEquals(expectedName, actualName);

        expectedName = "UNKNOWN";
        actualName = NetworkUtility.getPayloadTypeName(0);
    }

}
