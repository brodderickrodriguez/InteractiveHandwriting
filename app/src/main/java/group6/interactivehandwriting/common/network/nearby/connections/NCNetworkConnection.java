package group6.interactivehandwriting.common.network.nearby.connections;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import group6.interactivehandwriting.common.app.Profile;

// TODO resolve if certain network components need their own thread
public class NCNetworkConnection {
    private final String V_TAG = "NCNetworkConnection";
    private final String SERVICE_ID = "group6.interactive.handwriting";
    private final Strategy NEARBY_CONNECTIONS_STRATEGY = Strategy.P2P_CLUSTER;

    private ConnectionsClient connectionClient;

    private NCNetworkLayerService manager;
    private String deviceName;

    public NCNetworkConnection forService(NCNetworkLayerService service) {
        this.manager = service;
        return this;
    }

    public NCNetworkConnection withProfile(Profile profile) {
        this.deviceName = profile.username;
        return this;
    }

    public void begin(Context context) {
        Log.v(V_TAG, "Starting network service");
        this.connectionClient = Nearby.getConnectionsClient(context);
        advertise();
    }

    private void advertise() {
        Task<Void> advertiseTask = connectionClient.startAdvertising(
                deviceName,
                SERVICE_ID,
                getConnectionLifeCycleCallback(),
                getAdvertisingOptions()
        );
        advertiseTask.addOnSuccessListener(getAdvertisingSucceededCallback());
        advertiseTask.addOnFailureListener(getAdvertisingFailedCallback());
    }

    private ConnectionLifecycleCallback getConnectionLifeCycleCallback() {
        return new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                boolean shouldAddConnection = manager.onConnectionInitiated(endpointId);
                if (shouldAddConnection) {
                    connectionClient.acceptConnection(endpointId, getPayloadCallback());
                }
            }

            @Override
            public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution connectionResolution) {
                manager.onConnectionResult(endpointId, connectionResolution.getStatus());
            }

            @Override
            public void onDisconnected(@NonNull String endpointId) {
                manager.onDisconnected(endpointId);
            }
        };
    }

    private PayloadCallback getPayloadCallback() {
        return new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
                // TODO add logic for handling destinations that are not neighbors
                manager.receiveMessage(endpointId, payload);
            }

            @Override
            public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate update) {
//                if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
//                    long payloadId = update.getPayloadId();
//                    Payload payload = incomingFilePayloads.remove(payloadId);
//                    completedFilePayloads.put(payloadId, payload);
//                    if (payload.getType() == Payload.Type.FILE) {
//                        manager.receiveFile(payloadId);
//                    }
//                }

            }
        };
    }

    private AdvertisingOptions getAdvertisingOptions() {
        return new AdvertisingOptions.Builder()
                .setStrategy(NEARBY_CONNECTIONS_STRATEGY)
                .build();
    }

    private OnSuccessListener<Void> getAdvertisingSucceededCallback() {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // TODO what happens when the advertising begins successfully
                discover();
            }
        };
    }

    private OnFailureListener getAdvertisingFailedCallback() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO what happens when the advertising fails to initiate
            }
        };
    }

    private void discover() {
        Task<Void> discoverTask = connectionClient.startDiscovery(
                SERVICE_ID,
                getEndpointDiscoveryCallback(),
                getDiscoveryOptions()
        );

        discoverTask.addOnSuccessListener(getDiscoverySucceededCallback());
        discoverTask.addOnFailureListener(getDiscoveryFailedCallback());
    }

    private EndpointDiscoveryCallback getEndpointDiscoveryCallback() {
        return new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NonNull String deviceName, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                if (discoveredEndpointInfo.getServiceId().equals(SERVICE_ID)) {
                    Task<Void> requestConnectionTask = connectionClient.requestConnection(
                            deviceName,
                            deviceName,
                            getConnectionLifeCycleCallback()
                    );
                    requestConnectionTask.addOnSuccessListener(getRequestConnectionSucceededCallback());
                    requestConnectionTask.addOnFailureListener(getRequestConnectionFailedCallback());
                }
            }

            @Override
            public void onEndpointLost(@NonNull String s) {
                // TODO remote endpoint has disconnected or become unreachable
            }
        };
    }

    private OnSuccessListener<Void> getRequestConnectionSucceededCallback() {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // TODO request succeeded
            }
        };
    }

    private OnFailureListener getRequestConnectionFailedCallback() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO request failed
            }
        };
    }

    private DiscoveryOptions getDiscoveryOptions() {
        return new DiscoveryOptions.Builder()
                .setStrategy(NEARBY_CONNECTIONS_STRATEGY)
                .build();
    }

    private OnSuccessListener<Void> getDiscoverySucceededCallback() {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // TODO what to do if discovery begins
            }
        };
    }

    private OnFailureListener getDiscoveryFailedCallback() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO what to do if discovery fails to begin
            }
        };
    }

    public void sendMessage(Payload message, List<String> endpointIds) {
        connectionClient.sendPayload(endpointIds, message);
    }

    public void sendFile(Payload filePayload, List<String> endpointIds) {
        connectionClient.sendPayload(endpointIds, filePayload);
    }
}
