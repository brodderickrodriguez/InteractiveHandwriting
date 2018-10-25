package group6.interactivehandwriting.common.network;

import org.junit.Test;

import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkUtility;

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
        actualName = NCNetworkUtility.getPayloadTypeName(1);
        assertEquals(expectedName, actualName);

        expectedName = "FILE";
        actualName = NCNetworkUtility.getPayloadTypeName(2);
        assertEquals(expectedName, actualName);

        expectedName = "STREAM";
        actualName = NCNetworkUtility.getPayloadTypeName(3);
        assertEquals(expectedName, actualName);

        expectedName = "UNKNOWN";
        actualName = NCNetworkUtility.getPayloadTypeName(0);
    }

}
