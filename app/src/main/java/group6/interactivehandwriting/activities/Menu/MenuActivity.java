package group6.interactivehandwriting.activities.Menu;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.RoomActivity;
import group6.interactivehandwriting.common.app.Permissions;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkLayer;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;

public class MenuActivity extends AppCompatActivity {
    NetworkLayer networkLayer;
    ServiceConnection networkServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        networkServiceConnection = getNetworkServiceConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Permissions.requestPermissions(this);

        NetworkLayerService.startNetworkService(this);
        NetworkLayerService.bindNetworkService(this, networkServiceConnection);
    }

    private ServiceConnection getNetworkServiceConnection() {
        return new ServiceConnection()
        {
            @Override
            public void onServiceConnected (ComponentName name, IBinder service){
                NetworkLayerBinder binder = (NetworkLayerBinder) service;
                networkLayer = binder.getNetworkLayer();
                networkLayer.begin(new Profile());
                handleNetworkStarted();
            }

            @Override
            public void onServiceDisconnected (ComponentName name) {
            }
        };
    }

    private void handleNetworkStarted() {
        // TODO once the network has begun working
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(networkServiceConnection);
    }

    public void enterRoom(View view) {
        Intent drawingActivity = new Intent(this, RoomActivity.class);
        startActivity(drawingActivity);
    }

    /** Handles user acceptance (or denial) of our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != Permissions.REQUEST_CODE_REQUIRED_PERMISSIONS) {
        } else {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    finish();
                }
            }

        }


        recreate();
    }
}
