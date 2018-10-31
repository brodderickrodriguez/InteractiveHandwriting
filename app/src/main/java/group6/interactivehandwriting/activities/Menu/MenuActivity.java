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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Menu.adapaters.RoomAdapter;
import group6.interactivehandwriting.activities.Room.RoomActivity;
import group6.interactivehandwriting.common.app.Permissions;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.rooms.Room;
import group6.interactivehandwriting.common.network.NetworkLayer;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;

public class MenuActivity extends AppCompatActivity {
    private static final long REQUEST_ROOMS_DELAY_MS = 0;
    private static final long REQUEST_ROOMS_INTERVAL_MS = 1000;

    NetworkLayer networkLayer;
    ServiceConnection networkServiceConnection;
    Profile myProfile;
    Timer roomRequestTimer;
    RoomAdapter roomAdapter;
    ListView roomListView;
    TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        networkServiceConnection = getNetworkServiceConnection();
        myProfile = new Profile();
        myProfile.deviceId = Profile.DEVICE_ID;
        myProfile.username = "Jake Laney";
        roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.room_button);
        roomListView = findViewById(R.id.room_list);
        roomListView.setAdapter(roomAdapter);
        roomListView.setOnItemClickListener(roomSelectedListener());
        usernameText = findViewById(R.id.username);
    }

    private AdapterView.OnItemClickListener roomSelectedListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room selectedRoom = roomAdapter.getItem(position);
                joinRoom(selectedRoom);
            }
        };
    }

    private void joinRoom(Room selectedRoom) {
        if (networkLayer != null) {
            updateUsername();
            networkLayer.joinRoom(myProfile, selectedRoom);
            enterRoom(selectedRoom);
        }
    }

    public void enterRoom(Room room) {
        Intent drawingActivity = new Intent(this, RoomActivity.class);
        drawingActivity.putExtra("ROOM_NAME", room.name);
        startActivity(drawingActivity);
    }

    public void showNewRoomPopup(View view) {
        if (networkLayer != null) {
            Room defaultRoom = new Room();
            defaultRoom.deviceId = myProfile.deviceId;
            defaultRoom.name = "Default Room";
            joinRoom(defaultRoom);
        }
    }

    private void updateUsername() {
        myProfile.username = usernameText.getText().toString();
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
                networkLayer.begin(myProfile); // TODO
                handleNetworkStarted();
            }

            @Override
            public void onServiceDisconnected (ComponentName name) {
            }
        };
    }

    private void handleNetworkStarted() {
        startRequestingRooms();
    }

    private void startRequestingRooms() {
        roomRequestTimer = new Timer();
        roomRequestTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateRoomList(networkLayer.getRooms());
            }
        }, REQUEST_ROOMS_DELAY_MS, REQUEST_ROOMS_INTERVAL_MS);
    }

    private void updateRoomList(Set<Room> discoveredRooms) {
        // TODO add a listview and adapter
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(networkServiceConnection);
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
