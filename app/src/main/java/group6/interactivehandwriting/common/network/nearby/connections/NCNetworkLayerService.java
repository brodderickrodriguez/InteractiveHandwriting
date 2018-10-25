package group6.interactivehandwriting.common.network.nearby.connections;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.activities.Room.actions.draw.DrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.EndDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.StartDrawAction;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;
import group6.interactivehandwriting.common.network.nearby.connections.device.NCDevice;
import group6.interactivehandwriting.common.network.nearby.connections.device.NCDeviceManager;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.NetworkSerializable;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.SerialMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.SerialMessageHeader;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw.EndDrawActionMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw.MoveDrawActionMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw.StartDrawActionMessage;

/**
 * Created by JakeL on 9/30/18.
 */

// TODO we should definitely create an object that encapsulates the HEADER, DATA section pair for byte[]
public class NCNetworkLayerService extends NetworkLayerService<Payload> {
    private static final String DEBUG_TAG = "NCNetworkLayerService";

    private static final int HEADER_LENGTH_BYTES = 4;

    private Context context;
    private NCDeviceManager deviceManager;

    private NCNetworkConnection networkConnection;

    private CanvasManager canvasManager;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public NetworkLayerBinder onBind(Intent bindIntent) {
        Log.v(DEBUG_TAG, "Binding NC Network Layer ");
        return new NCNetworkLayerBinder(this);
    }

    @Override
    public void begin(Profile profile) {
        if (networkConnection == null) {
            context = getApplicationContext();
            Toast.makeText(context, "Starting Network Service", Toast.LENGTH_LONG).show();
            deviceManager = new NCDeviceManager();
            networkConnection = new NCNetworkConnection(context, profile, this);
        }
    }

    @Override
    public void setCanvasManager(CanvasManager canvasManager) {
        this.canvasManager = canvasManager;
    }

    @Override
    public void startDraw(StartDrawAction action) {
        sendSerialMessage(StartDrawActionMessage.fromAction(action));
    }

    @Override
    public void moveDraw(MoveDrawAction action) {
        sendSerialMessage(MoveDrawActionMessage.fromAction(action));
    }

    @Override
    public void endDraw(EndDrawAction action) {
        sendSerialMessage(EndDrawActionMessage.fromAction(action));
    }

    public void sendSerialMessage(NetworkSerializable data) {
        SerialMessageHeader header = new SerialMessageHeader();
        header.withRoomNumber(0).withSequenceNumber(0).withType(data.getType());

        SerialMessage message = new SerialMessage();
        message.withHeader(header).withData(data);

        Payload payload = Payload.fromBytes(message.toBytes());
        networkConnection.sendMessage(payload, deviceManager.getDevices());
    }


    public boolean isSet(Object o) {
        return o != null;
    }

    public void receiveMessage(Payload payload) {
        switch(payload.getType()) {
            case Payload.Type.BYTES:
                handleBytesPayload(payload.asBytes());
                break;
            default:
                break;
        }
    }

    private void handleBytesPayload(byte[] payloadBytes) {
        SerialMessage message = (new SerialMessage()).fromBytes(payloadBytes);
        SerialMessageHeader header = (new SerialMessageHeader()).fromBytes(message.getHeader());
        dispatch(header, message.getData());
    }

    private void dispatch(SerialMessageHeader header, byte[] dataSection) {
        String deviceName = String.valueOf(header.getDeviceId());
        switch(header.getType()) {
            case START_DRAW:
                sendActionToCanvasManager(deviceName, StartDrawActionMessage.actionFromBytes(dataSection));
                break;
            case MOVE_DRAW:
                sendActionToCanvasManager(deviceName, MoveDrawActionMessage.actionFromBytes(dataSection));
                break;
            case END_DRAW:
                sendActionToCanvasManager(deviceName, EndDrawActionMessage.actionFromBytes(dataSection));
                break;
            default:
                break;
        }
    }

    private void sendActionToCanvasManager(String deviceName, DrawAction action) {
        if (isSet(canvasManager)) {
            canvasManager.putAction(deviceName, action);
        } else {
            Log.e(DEBUG_TAG, "Message received but canvas manager is not set");
        }
    }

    public boolean onConnectionInitiated(NCDevice device) {
        Toast.makeText(context, "Device found with name" + device.getDeviceName(), Toast.LENGTH_SHORT).show();
        return deviceManager.shouldAcceptConnection(device);
    }

    public void onConnectionResult(NCDevice device, Status connectionStatus) {
        Toast.makeText(context, "Device connected with name " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
        deviceManager.addDevice(device, connectionStatus);
    }

    public void onDisconnected(NCDevice device) {
        Toast.makeText(context, "Device disconnected with name " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
        deviceManager.disconnectDevice(device);
    }
}
