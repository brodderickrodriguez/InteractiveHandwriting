package group6.interactivehandwriting.common.network.nearby.connections;

/**
 * Created by JakeL on 9/23/18.
 */

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NCDeviceManagerTest {
    @Test
    public void test_addDevice_STATUS_OK() {
        String deviceName = "deviceName";
        NCDevice device = new NCDevice(deviceName);
        Status statusOk = NCDeviceManagerTest.getStatusOk();
        NCDeviceManager NCDeviceManager = new NCDeviceManager();

        Boolean wasDeviceAdded = NCDeviceManager.addDevice(device, statusOk);
        assertTrue(wasDeviceAdded);

        wasDeviceAdded = NCDeviceManager.addDevice(device, statusOk);
        assertFalse(wasDeviceAdded);

        String nextDeviceName = "nextDeviceName";
        NCDevice nextDevice = new NCDevice(nextDeviceName);
        wasDeviceAdded = NCDeviceManager.addDevice(nextDevice, statusOk);
        assertTrue(wasDeviceAdded);

        List<NCDevice> entries = NCDeviceManager.getDeviceEntries();
        assertTrue(entries.contains(new NCDevice(deviceName)));
        assertTrue(entries.contains(new NCDevice(nextDeviceName)));
        assertEquals(2, entries.size());
    }

    @Test
    public void test_addDevice_STATUS_ERROR() {
        String deviceName = "deviceName";
        NCDevice device = new NCDevice(deviceName);
        Status statusError = NCDeviceManagerTest.getStatusError();
        NCDeviceManager NCDeviceManager = new NCDeviceManager();

        Boolean wasDeviceAdded = NCDeviceManager.addDevice(device, statusError);
        assertFalse(wasDeviceAdded);

        List<NCDevice> entries = NCDeviceManager.getDeviceEntries();
        assertEquals(0, entries.size());
    }

    @Test
    public void test_disconnectDevice() {
        String deviceName = "deviceName";
        NCDevice device = new NCDevice(deviceName);
        NCDeviceManager NCDeviceManager = new NCDeviceManager();

        Boolean wasDeviceRemoved = NCDeviceManager.disconnectDevice(device);
        assertFalse(wasDeviceRemoved);

        NCDeviceManager.addDevice(device, NCDeviceManagerTest.getStatusOk());
        wasDeviceRemoved = NCDeviceManager.disconnectDevice(device);
        assertTrue(wasDeviceRemoved);
        assertEquals(0, NCDeviceManager.getDeviceEntries().size());

        wasDeviceRemoved = NCDeviceManager.disconnectDevice(device);
        assertFalse(wasDeviceRemoved);
    }

    @Test
    public void test_shouldAcceptConnection() {
        String deviceName = "deviceName";
        NCDevice device = new NCDevice(deviceName);
        NCDeviceManager NCDeviceManager = new NCDeviceManager();

        Boolean shouldAcceptConnection = NCDeviceManager.shouldAcceptConnection(device);
        assertTrue(shouldAcceptConnection);

        NCDeviceManager.addDevice(device, NCDeviceManagerTest.getStatusOk());

        shouldAcceptConnection = NCDeviceManager.shouldAcceptConnection(device);
        assertFalse(shouldAcceptConnection);

        NCDeviceManager.disconnectDevice(device);

        shouldAcceptConnection = NCDeviceManager.shouldAcceptConnection(device);
        assertTrue(shouldAcceptConnection);
    }

    @Test
    public void test_getNeighboringDeviceNames() {
        String deviceName = "deviceName";
        NCDevice device = new NCDevice(deviceName);
        NCDeviceManager NCDeviceManager = new NCDeviceManager();

        NCDeviceManager.addDevice(device, NCDeviceManagerTest.getStatusOk());

        List<String> names = NCDeviceManager.getNeighboringDeviceNames();
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
