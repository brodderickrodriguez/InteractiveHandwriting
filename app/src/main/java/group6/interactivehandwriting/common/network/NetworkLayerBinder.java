package group6.interactivehandwriting.common.network;

import android.os.Binder;

/**
 * Created by JakeL on 10/17/18.
 */

public abstract class NetworkLayerBinder extends Binder {
    public abstract NetworkLayer getNetworkLayer();
}
