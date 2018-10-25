package group6.interactivehandwriting.common.network;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

/**
 * Created by JakeL on 10/15/18.
 */

public abstract class NetworkLayerService<T> extends Service implements NetworkLayer<T> {
    public abstract NetworkLayerBinder onBind(Intent bindIntent);

    public static void startNetworkService(Activity activity) {
        Intent startIntent = getServiceIntent(activity);
        activity.startService(startIntent);
    }

    public static void bindNetworkService(Activity activity, ServiceConnection connectionCallback) {
        Intent bindIntent = getServiceIntent(activity);
        activity.bindService(bindIntent, connectionCallback, Context.BIND_AUTO_CREATE);
    }

    private static Intent getServiceIntent(Activity activity) {
        return new Intent(activity, NetworkConfiguration.NETWORK_LAYER_SERVICE_CLASS);
    }
}
