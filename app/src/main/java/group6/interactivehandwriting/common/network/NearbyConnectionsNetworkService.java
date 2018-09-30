package group6.interactivehandwriting.common.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.endpoint.Endpoint;
import group6.interactivehandwriting.common.network.endpoint.NearbyConnectionsEndpoint;

// TODO resolve if certain network components need their own thread
public class NearbyConnectionsNetworkService implements NetworkService<Payload> {
    private final String SERVICE_ID = "group6.interactive.handwriting";
    private final Strategy NEARBY_CONNECTIONS_STRATEGY = Strategy.P2P_CLUSTER;

    private ConnectionsClient connectionClient;

    private NetworkManager<Payload> networkServiceManager;
    private String deviceName;

    public NearbyConnectionsNetworkService(Context context, Profile profile) {
        connectionClient = Nearby.getConnectionsClient(context);
        deviceName = profile.getDeviceName();
    }

    public void setNetworkServiceManager(NetworkManager<Payload> manager) {
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
                NearbyConnectionsEndpoint endpoint = new NearbyConnectionsEndpoint(endpointName);
                boolean shouldAddConnection = networkServiceManager.onConnectionInitiated(endpoint);

                if (shouldAddConnection) {
                    connectionClient.acceptConnection(endpointName, getPayloadCallback());
                }
            }

            @Override
            public void onConnectionResult(@NonNull String endpointName, @NonNull ConnectionResolution connectionResolution) {
                NearbyConnectionsEndpoint endpoint = new NearbyConnectionsEndpoint(endpointName, connectionResolution);
                networkServiceManager.onConnectionResult(endpoint);

            }

            @Override
            public void onDisconnected(@NonNull String endpointName) {
                NearbyConnectionsEndpoint endpoint = new NearbyConnectionsEndpoint(endpointName);
                networkServiceManager.onDisconnected(endpoint);
            }
        };
    }

    private PayloadCallback getPayloadCallback() {
        return new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String endpointName, @NonNull Payload payload) {
                // TODO add logic for handling destinations that are not neighbors
                NearbyConnectionsEndpoint endpoint = new NearbyConnectionsEndpoint(endpointName);
                networkServiceManager.receiveMessage(payload, endpoint);
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
                if (discoveredEndpointInfo.getEndpointName().equals(SERVICE_ID)) {
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


    @Override
    public void setNetworkManager(NetworkManager manager) {
        networkServiceManager = (NetworkManager<Payload>) manager;
    }

    @Override
    public void sendMessage(Payload message, List<String> endpoints) {
        connectionClient.sendPayload(endpoints, message);
    }
}
