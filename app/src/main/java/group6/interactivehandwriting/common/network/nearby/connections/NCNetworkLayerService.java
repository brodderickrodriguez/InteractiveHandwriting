package group6.interactivehandwriting.common.network.nearby.connections;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.Payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import group6.interactivehandwriting.common.app.actions.Action;
import group6.interactivehandwriting.common.app.actions.DrawActionHandle;
import group6.interactivehandwriting.common.app.actions.draw.DrawableAction;
import group6.interactivehandwriting.common.app.actions.draw.EndDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.StartDrawAction;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.rooms.Room;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;
import group6.interactivehandwriting.common.network.nearby.connections.device.NCRoutingTable;
import group6.interactivehandwriting.common.network.nearby.connections.message.NetworkMessageType;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.NetworkSerializable;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.SerialMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.SerialMessageHeader;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.SerialMessageData;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw.EndDrawActionMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw.MoveDrawActionMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw.StartDrawActionMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.draw.UndoDrawMessage;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.room.RoomSynchronizeRequest;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.routing.RoutingUpdate;
import group6.interactivehandwriting.common.network.nearby.connections.message.serial.data.routing.RoutingUpdateRequest;

/**
 * Created by JakeL on 9/30/18.
 */

// TODO we should definitely create an object that encapsulates the HEADER, DATA section pair for byte[]
public class NCNetworkLayerService extends NetworkLayerService {
    private static boolean isActive = false;

    private NCRoutingTable routingTable;

    private NCNetworkConnection networkConnection;

    private Context context;
    private Profile myProfile;
    private Room myRoom;

    private DrawActionHandle drawActionHandle;

    public boolean onConnectionInitiated(String endpointId) {
        Toast.makeText(context, "Device found with id " + endpointId, Toast.LENGTH_SHORT).show();

        return true; // TODO manage endpoint handshakes
    }

    public void onConnectionResult(String endpointId, Status connectionStatus) {
        Toast.makeText(context, "Device connected with endpoint id " + endpointId, Toast.LENGTH_SHORT).show();
        sendSerialMessage(new RoutingUpdateRequest(), endpointId);
    }

    public void onDisconnected(String endpointId) {
        Toast.makeText(context, "Device disconnected with id " + endpointId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public NetworkLayerBinder onBind(Intent bindIntent) {
        return new NCNetworkLayerBinder(this);
    }

    @Override
    public void begin(final Profile profile) {
        if (!isActive) {
            isActive = true;
            this.myRoom = new Room();
            this.myProfile = profile;
            this.context = getApplicationContext();
            this.routingTable = new NCRoutingTable();
            this.routingTable.setMyProfile(profile);
            this.networkConnection = new NCNetworkConnection()
                    .forService(this)
                    .withProfile(profile);
            this.networkConnection.begin(context);

            Toast.makeText(context, "Starting Network Service", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Profile getMyProfile() {
        return myProfile;
    }

    @Override
    public Set<Room> getRooms() {
        sendSerialMessage(new RoutingUpdateRequest());
        Set<Room> rooms = routingTable.getRooms();
        return rooms;
    }

    @Override
    public void joinRoom(final Profile profile, final Room room) {
        myRoom = new Room(room.deviceId, room.name);
        routingTable.setMyRoom(profile, myRoom);
        sendRoutingUpdate();
    }

    @Override
    public void synchronizeRoom() {
        sendSerialMessage(new RoomSynchronizeRequest().withRoomNumber(myRoom.getRoomNumber()));
    }

    private void synchronizeRoomReply(String endpoint) {
        List<Action> actionHistory = drawActionHandle.getActionHistory();
        List<SerialMessageData> messages = new ArrayList<>();

        for (Action act : actionHistory) {
            if (act instanceof StartDrawAction) {
                messages.add(StartDrawActionMessage.fromAction((StartDrawAction) act));
            } else if (act instanceof MoveDrawAction) {
                messages.add(MoveDrawActionMessage.fromAction((MoveDrawAction) act));
            } else if (act instanceof EndDrawAction) {
                messages.add(EndDrawActionMessage.fromAction((EndDrawAction) act));
            }
        }

        for (SerialMessageData data : messages) {
            sendSerialMessage(data, endpoint);
        }
    }

    @Override
    public void exitRoom() {
        // cleanup
        routingTable.exitMyRoom(myProfile);
        myRoom = new Room();
        drawActionHandle = null;
        sendRoutingUpdate();
    }

    @Override
    public void sendPDf(byte[][] bitmapByteArrays) {
        for (byte[] bitmapByteArray : bitmapByteArrays) {
            SerialMessageHeader header = new SerialMessageHeader()
                    .withId(myProfile.deviceId)
                    .withRoomNumber(myRoom.getRoomNumber())
                    .withSequenceNumber(SerialMessageHeader.getNextSequenceNumber())
                    .withType(NetworkMessageType.SEND_PDF);

            SerialMessage message = new SerialMessage();
            message.withHeader(header).withData(bitmapByteArray);

            Payload payload = Payload.fromBytes(message.toBytes());
            networkConnection.sendMessage(payload, routingTable.getNeighborEndpoints());
        }
    }


    @Override
    public void receiveDrawActions(DrawActionHandle handle) {
        this.drawActionHandle = handle;
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

    @Override
    public void undo(Profile p) {
        sendSerialMessage(new UndoDrawMessage().withProfile(p));
    }

    public void sendSerialMessage(NetworkSerializable data) {
        sendSerialMessage(data, routingTable.getNeighborEndpoints());
    }

    public void sendSerialMessage(NetworkSerializable data, final String endpoint) {
        List<String> endpoints = new ArrayList<>();
        endpoints.add(endpoint);
        sendSerialMessage(data, endpoints);
    }

    public void sendSerialMessage(NetworkSerializable data, List<String> endpoints) {
        SerialMessageHeader header = new SerialMessageHeader()
                .withId(myProfile.deviceId)
                .withRoomNumber(myRoom.getRoomNumber())
                .withSequenceNumber(SerialMessageHeader.getNextSequenceNumber())
                .withType(data.getType());

        SerialMessage message = new SerialMessage();
        message.withHeader(header).withData(data);

        Payload payload = Payload.fromBytes(message.toBytes());
        networkConnection.sendMessage(payload, endpoints);
    }

    public void receiveMessage(String endpoint, Payload payload) {
        switch(payload.getType()) {
            case Payload.Type.BYTES:
                handleBytesPayload(endpoint, payload.asBytes());
                break;
            default:
                break;
        }
    }

    private void handleBytesPayload(String endpoint, byte[] payloadBytes) {
        SerialMessage message = new SerialMessage().fromBytes(payloadBytes);
        SerialMessageHeader header = new SerialMessageHeader().fromBytes(message.getHeader());
        byte[] data = message.getData();

        if (myRoom != null &&
                myRoom.getRoomNumber() != Room.VOID_ROOM_NUMBER &&
                header.getRoomNumber() == myRoom.getRoomNumber()) {
            dispatchRoomMessage(endpoint, header, data);
        } else {
            dispatchMessage(endpoint, header, data);
        }
    }

    private void dispatchRoomMessage(String endpoint, SerialMessageHeader header, byte[] dataSection) {
        long id = header.getDeviceId();
        switch(header.getType()) {
            case SEND_PDF:
                System.out.print("PDF received");
            case START_DRAW:
                sendActionToCanvasManager(id, StartDrawActionMessage.actionFromBytes(dataSection));
                break;
            case MOVE_DRAW:
                sendActionToCanvasManager(id, MoveDrawActionMessage.actionFromBytes(dataSection));
                break;
            case END_DRAW:
                sendActionToCanvasManager(id, EndDrawActionMessage.actionFromBytes(dataSection));
                break;
            case UNDO_DRAW:
                if (drawActionHandle != null) {
                    drawActionHandle.undo(UndoDrawMessage.undoFromBytes(dataSection).getData());
                }
                break;
            case SYNC_REQUEST:
                synchronizeRoomReply(endpoint);
            default:
                break;
        }
    }

    private void dispatchMessage(String endpoint, SerialMessageHeader header, byte[] dataSection) {
        switch(header.getType()) {
            case ROUTING_UPDATE_REQUEST:
                sendRoutingUpdate(endpoint);
                break;
            case ROUTING_UPDATE_REPLY:
                handleRoutingUpdateReply(endpoint, new RoutingUpdate().fromBytes(dataSection));
                break;
            default:
                break;
        }
    }

    private void sendRoutingUpdate() {
        sendSerialMessage(new RoutingUpdate().withTable(routingTable));
    }

    private void sendRoutingUpdate(String endpoint) {
        sendSerialMessage(new RoutingUpdate().withTable(routingTable), endpoint);
    }


    private void handleRoutingUpdateReply(String endpoint, RoutingUpdate updateMessage) {
        routingTable.update(endpoint, updateMessage.getData());
    }

    private void sendActionToCanvasManager(long deviceId, DrawableAction action) {
        if (drawActionHandle != null) {
            drawActionHandle.handleDrawAction(routingTable.getProfile(deviceId), action);
        }
    }
}
