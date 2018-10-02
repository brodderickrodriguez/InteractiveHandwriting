package group6.interactivehandwriting.common.network.nearby.connections;

/**
 * Created by JakeL on 9/23/18.
 */

import org.junit.Test;

import group6.interactivehandwriting.common.network.nearby.connections.NCDevice;

import static org.junit.Assert.*;

public class NCDeviceTest {
    @Test
    public void test_deviceName() {
        String deviceName = "deviceName";
        String nextDeviceName = "nextDeviceName";
        NCDevice entry = new NCDevice(deviceName);
        assertEquals(deviceName, entry.getDeviceName());
        entry.setDeviceName(nextDeviceName);
        assertEquals(nextDeviceName, entry.getDeviceName());
    }

    @Test
    public void test_equals() {
        String deviceNameOne = "device1";
        String deviceNameTwo = "device2";
        NCDevice entryOne = new NCDevice(deviceNameOne);
        NCDevice entryTwo = new NCDevice(deviceNameTwo);
        NCDevice entryEqualsOne = new NCDevice(deviceNameOne);

        assertNotEquals(entryOne, entryTwo);
        assertEquals(entryOne, entryOne);
        assertEquals(entryOne, entryEqualsOne);
    }
}
