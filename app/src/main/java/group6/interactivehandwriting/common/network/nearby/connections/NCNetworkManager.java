package group6.interactivehandwriting.common.network.nearby.connections;

import android.content.Context;

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

/**
 * Created by JakeL on 9/30/18.
 */

public class NCNetworkManager implements NetworkManager<Payload> {
    private static final int HEADER_LENGTH_BYTES = 4;

    private NCDeviceManager deviceManager;
    private NCNetworkService networkService;
    private CanvasManager canvasManager;

    public NCNetworkManager(Context context, Profile profile) {
        networkService = new NCNetworkService(context, profile, this);
        deviceManager = new NCDeviceManager();
    }

    public void setCanvasManager(CanvasManager canvasManager) {
        this.canvasManager = canvasManager;
    }

    @Override
    public void setNetworkService(NetworkService service) {
        networkService = (NCNetworkService) service;
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
        dispatch( NetworkMessageType.fromValue(typeValue), payloadBytes, device);
    }

    private void dispatch(NetworkMessageType messageType, byte[] bytes, NCDevice device) {
        switch(messageType) {
            case START_DRAW:
            case MOVE_DRAW:
            case END_DRAW:
                dispatchToCanvasManager(messageType, bytes, device);
            default:
                break;
        }
    }

    private void dispatchToCanvasManager(NetworkMessageType messageType, byte[] bytes, NCDevice device) {
        DrawAction action = null;
        switch(messageType) {
            case START_DRAW:
                action = new StartDrawAction(false);
            case MOVE_DRAW:
                action = new MoveDrawAction();
            case END_DRAW:
                action = new EndDrawAction();
            default:
                break;
        }
        if (action != null && canvasManager != null) {
            action.unpack(bytes);
            canvasManager.putAction(device.getDeviceName(), action);
        }
    }

    @Override
    public void sendMessage(NetworkMessage message) {
        byte[] messageBytes = createByteMessage(message);
        Payload payload = Payload.fromBytes(messageBytes);
        networkService.sendMessage(payload, deviceManager.getDevices());
    }

    private byte[] createByteMessage(NetworkMessage<byte[]> message) {
        byte[] header = createHeader(message);
        byte[] data = (byte[]) message.pack();
        return joinByteArrays(header, data);
    }

    private byte[] createHeader(NetworkMessage<byte[]> message) {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH_BYTES);
        buffer.putInt(message.getType().getValue());
        return buffer.array();
    }

    private byte[] joinByteArrays(byte[] one, byte[] two) {
        int length = one.length + two.length;
        byte[] result = new byte[length];

        for (int i = 0; i < one.length; i++) {
            result[i] = one[i];
        }
        for (int i = one.length; i < length; i++) {
            result[i] = two[i - one.length];
        }

        return result;
    }

    @Override
    public boolean onConnectionInitiated(NCDevice device) {
        return deviceManager.shouldAcceptConnection(device);
    }

    @Override
    public void onConnectionResult(NCDevice device, Status connectionStatus) {
        deviceManager.addDevice(device, connectionStatus);
    }

    @Override
    public void onDisconnected(NCDevice device) {
        deviceManager.disconnectDevice(device);
    }
}
