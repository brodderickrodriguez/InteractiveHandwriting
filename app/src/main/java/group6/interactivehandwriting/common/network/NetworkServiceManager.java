package group6.interactivehandwriting.common.network;

import com.google.android.gms.nearby.connection.Payload;

/**
 * Created by JakeL on 9/22/18.
 */

public interface NetworkServiceManager {
    public void handlePayload(Payload payload);
}
