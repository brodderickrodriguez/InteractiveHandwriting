package group6.interactivehandwriting.common.network.nearby.connections;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.Payload;

import java.nio.ByteBuffer;

import group6.interactivehandwriting.activities.Room.actions.draw.DrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.EndDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.activities.Room.actions.draw.StartDrawAction;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkManager;
import group6.interactivehandwriting.common.network.NetworkMessage;
import group6.interactivehandwriting.common.network.NetworkMessageType;
import group6.interactivehandwriting.common.network.NetworkService;
import group6.interactivehandwriting.common.network.NetworkUtility;

/**
 * Created by JakeL on 9/30/18.
 */

// TODO we should definitely create an object that encapsulates the HEADER, DATA section pair for byte[]
public class NCNetworkManager implements NetworkManager<Payload> {
    private static final String E_TAG = "NCNetworkManager";
    private static final int HEADER_LENGTH_BYTES = 4;

    private Context context;
    private NCDeviceManager deviceManager;
    private NCNetworkService networkService;
    private CanvasManager canvasManager;

    public NCNetworkManager(Context context, Profile profile) {
        this.context = context;
        this.networkService = new NCNetworkService(context, profile, this);
        this.deviceManager = new NCDeviceManager();
    }

    public void setCanvasManager(CanvasManager canvasManager) {
        this.canvasManager = canvasManager;
    }

    public boolean isSet(Object o) {
        return o != null;
    }

    @Override
    public boolean onConnectionInitiated(NCDevice device) {
        Toast.makeText(context, "Device found with name" + device.getDeviceName(), Toast.LENGTH_SHORT).show();
        return deviceManager.shouldAcceptConnection(device);
    }

    @Override
    public void onConnectionResult(NCDevice device, Status connectionStatus) {
        Toast.makeText(context, "Device connected with name " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
        deviceManager.addDevice(device, connectionStatus);
    }

    @Override
    public void onDisconnected(NCDevice device) {
        Toast.makeText(context, "Device disconnected with name " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
        deviceManager.disconnectDevice(device);
    }

    @Override
    public void receiveMessage(Payload payload, NCDevice device) {
        switch(payload.getType()) {
            case Payload.Type.BYTES:
                handleBytesPayload(payload.asBytes(), device);
                break;
            // TODO add files
            default:
                break;
        }
    }

    private void handleBytesPayload(byte[] payloadBytes, NCDevice device) {
        // TODO headers are not yet implemented fully
        // TODO only action messages for now
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        int typeValue = buffer.getInt();

        NetworkMessageType type = NetworkMessageType.fromValue(typeValue);
        byte[] dataSectionBytes = NetworkUtility.getDataSection(payloadBytes, HEADER_LENGTH_BYTES);
        dispatch(type, dataSectionBytes, device);
    }

    private void dispatch(NetworkMessageType messageType, byte[] dataSectionBytes, NCDevice device) {
        switch(messageType) {
            case START_DRAW:
            case MOVE_DRAW:
            case END_DRAW:
                dispatchToCanvasManager(messageType, dataSectionBytes, device);
                break;
            default:
                break;
        }
    }

    private void dispatchToCanvasManager(NetworkMessageType messageType, byte[] dataSectionBytes, NCDevice device) {
        DrawAction action = DrawAction.getDrawAction(messageType);
        action.unpack(dataSectionBytes);
        sendActionToCanvasManager(device.getDeviceName(), action);

    }

    private void sendActionToCanvasManager(String deviceName, DrawAction action) {
        if (isSet(canvasManager)) {
            canvasManager.putAction(deviceName, action);
        } else {
            Log.e(E_TAG, "Message received but canvas manager is not set");
        }
    }

    @Override
    public void sendMessage(NetworkMessage message) {
        byte[] messageBytes = NetworkUtility.createByteMessage(message, HEADER_LENGTH_BYTES);
        Payload payload = Payload.fromBytes(messageBytes);
        networkService.sendMessage(payload, deviceManager.getDevices());
    }
}
