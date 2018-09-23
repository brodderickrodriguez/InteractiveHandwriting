package group6.interactivehandwriting.common.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import group6.interactivehandwriting.common.app.Profile;

// TODO resolve if certain network components need their own thread
public class NetworkService {
    private final String SERVICE_ID = "group6.interactive.handwriting";
    private final Strategy NEARBY_CONNECTIONS_STRATEGY = Strategy.P2P_CLUSTER;

    private ConnectionsClient connectionClient;

    private NetworkServiceManager networkServiceManager;
    private NetworkDeviceManager networkDeviceManager;
    private String deviceName;

    public NetworkService(Context context, Profile profile) {
        connectionClient = Nearby.getConnectionsClient(context);
        deviceName = profile.getDeviceName();
        networkDeviceManager = new NetworkDeviceManager();
    }

    public void setNetworkServiceManager(NetworkServiceManager manager) {
        networkServiceManager = manager;
    }

    public void begin() {
        advertise();
        discover();
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
            public void onConnectionInitiated(@NonNull String endpointName, @NonNull ConnectionInfo connectionInfo) {
                if (networkDeviceManager.shouldAcceptConnection(endpointName)) {
                    connectionClient.acceptConnection(endpointName, getPayloadCallback());
                }
            }

            @Override
            public void onConnectionResult(@NonNull String endpointName, @NonNull ConnectionResolution connectionResolution) {
                networkDeviceManager.addDevice(endpointName, connectionResolution.getStatus());
            }

            @Override
            public void onDisconnected(@NonNull String endpointName) {
                networkDeviceManager.disconnectDevice(endpointName);
            }
        };
    }

    private PayloadCallback getPayloadCallback() {
        return new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String endpointName, @NonNull Payload payload) {
                // TODO add logic for handling destinations that are not neighbors
                networkServiceManager.handlePayload(payload);
            }

            @Override
            public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
                // TODO what to do if the payload has status updates (e.g. a FILE payload)
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
            public void onEndpointFound(@NonNull String endpointName, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                if (discoveredEndpointInfo.getEndpointName() == SERVICE_ID) {
                    Task<Void> requestConnectionTask = connectionClient.requestConnection(
                            deviceName,
                            endpointName,
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

    public void send(NetworkMessage message) {
        Payload payload = message.pack();
        List<String> endpoints = networkDeviceManager.getNeighboringDeviceNames();
        connectionClient.sendPayload(endpoints, payload);
    }
}
