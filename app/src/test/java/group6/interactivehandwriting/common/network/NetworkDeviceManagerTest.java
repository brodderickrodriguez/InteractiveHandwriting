package group6.interactivehandwriting.common.network;

/**
 * Created by JakeL on 9/23/18.
 */

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NetworkDeviceManagerTest {
    @Test
    public void test_addDevice_STATUS_OK() {
        String deviceName = "deviceName";
        Status statusOk = NetworkDeviceManagerTest.getStatusOk();
        NetworkDeviceManager networkDeviceManager = new NetworkDeviceManager();

        Boolean wasDeviceAdded = networkDeviceManager.addDevice(deviceName, statusOk);
        assertTrue(wasDeviceAdded);

        wasDeviceAdded = networkDeviceManager.addDevice(deviceName, statusOk);
        assertFalse(wasDeviceAdded);

        String nextDeviceName = "nextDeviceName";
        wasDeviceAdded = networkDeviceManager.addDevice(nextDeviceName, statusOk);
        assertTrue(wasDeviceAdded);

        List<NetworkDeviceEntry> entries = networkDeviceManager.getDeviceEntries();
        assertTrue(entries.contains(new NetworkDeviceEntry(deviceName)));
        assertTrue(entries.contains(new NetworkDeviceEntry(nextDeviceName)));
        assertEquals(2, entries.size());
    }

    // TODO increase test coverage

    public static Status getStatusOk() {
        return new Status(ConnectionsStatusCodes.STATUS_OK);
    }
}
