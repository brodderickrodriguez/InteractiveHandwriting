package group6.interactivehandwriting.common.network;

/**
 * Created by JakeL on 9/23/18.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class NetworkDeviceEntryTest {
    @Test
    public void test_deviceName() {
        String deviceName = "deviceName";
        String nextDeviceName = "nextDeviceName";
        NetworkDeviceEntry entry = new NetworkDeviceEntry(deviceName);
        assertEquals(deviceName, entry.getDeviceName());
        entry.setDeviceName(nextDeviceName);
        assertEquals(nextDeviceName, entry.getDeviceName());
    }

    @Test
    public void test_equals() {
        String deviceNameOne = "device1";
        String deviceNameTwo = "device2";
        NetworkDeviceEntry entryOne = new NetworkDeviceEntry(deviceNameOne);
        NetworkDeviceEntry entryTwo = new NetworkDeviceEntry(deviceNameTwo);
        NetworkDeviceEntry entryEqualsOne = new NetworkDeviceEntry(deviceNameOne);

        assertNotEquals(entryOne, entryTwo);
        assertEquals(entryOne, entryOne);
        assertEquals(entryOne, entryEqualsOne);
    }
}
