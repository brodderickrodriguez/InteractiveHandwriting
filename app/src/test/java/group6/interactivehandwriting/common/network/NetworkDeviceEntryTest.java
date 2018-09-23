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
}
