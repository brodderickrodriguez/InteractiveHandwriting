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

    @Test
    public void test_addDevice_STATUS_ERROR() {
        String deviceName = "deviceName";
        Status statusError = NetworkDeviceManagerTest.getStatusError();
        NetworkDeviceManager networkDeviceManager = new NetworkDeviceManager();

        Boolean wasDeviceAdded = networkDeviceManager.addDevice(deviceName, statusError);
        assertFalse(wasDeviceAdded);

        List<NetworkDeviceEntry> entries = networkDeviceManager.getDeviceEntries();
        assertEquals(0, entries.size());
    }

    @Test
    public void test_disconnectDevice() {
        String deviceName = "deviceName";
        NetworkDeviceManager networkDeviceManager = new NetworkDeviceManager();

        Boolean wasDeviceRemoved = networkDeviceManager.disconnectDevice(deviceName);
        assertFalse(wasDeviceRemoved);

        networkDeviceManager.addDevice(deviceName, NetworkDeviceManagerTest.getStatusOk());
        wasDeviceRemoved = networkDeviceManager.disconnectDevice(deviceName);
        assertTrue(wasDeviceRemoved);
        assertEquals(0, networkDeviceManager.getDeviceEntries().size());

        wasDeviceRemoved = networkDeviceManager.disconnectDevice(deviceName);
        assertFalse(wasDeviceRemoved);
    }

    @Test
    public void test_shouldAcceptConnection() {
        String deviceName = "deviceName";
        NetworkDeviceManager networkDeviceManager = new NetworkDeviceManager();

        Boolean shouldAcceptConnection = networkDeviceManager.shouldAcceptConnection(deviceName);
        assertTrue(shouldAcceptConnection);

        networkDeviceManager.addDevice(deviceName, NetworkDeviceManagerTest.getStatusOk());

        shouldAcceptConnection = networkDeviceManager.shouldAcceptConnection(deviceName);
        assertFalse(shouldAcceptConnection);

        networkDeviceManager.disconnectDevice(deviceName);

        shouldAcceptConnection = networkDeviceManager.shouldAcceptConnection(deviceName);
        assertTrue(shouldAcceptConnection);
    }

    @Test
    public void test_getNeighboringDeviceNames() {
        String deviceName = "deviceName";
        NetworkDeviceManager networkDeviceManager = new NetworkDeviceManager();

        networkDeviceManager.addDevice(deviceName, NetworkDeviceManagerTest.getStatusOk());

        List<String> names = networkDeviceManager.getNeighboringDeviceNames();
        assertEquals(1, names.size());
        assertTrue(names.contains(deviceName));
    }

    public static Status getStatusOk() {
        return new Status(ConnectionsStatusCodes.STATUS_OK);
    }

    public static Status getStatusError() {
        return new Status(ConnectionsStatusCodes.STATUS_ERROR);
    }
}
