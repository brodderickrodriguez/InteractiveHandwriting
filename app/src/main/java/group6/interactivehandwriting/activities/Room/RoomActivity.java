package group6.interactivehandwriting.activities.Room;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;

import group6.interactivehandwriting.R;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.w3c.dom.Document;

import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.views.RoomView;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkLayer;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;

// TODO move the file manipulation stuff to its own class
public class RoomActivity extends Activity {
    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };

    /* Request/Persmission Codes */
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    public static final int REQUEST_CODE_FILEPICKER = 2;

    private RoomView roomView;
    private SeekBar seekbar;
    private ColorPickerView color_picker_view;

    NetworkLayer networkLayer;
    ServiceConnection networkServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String roomName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                roomName = null;
            } else {
                roomName = extras.getString("ROOM_NAME");
            }
        } else {
            roomName = (String) savedInstanceState.getSerializable("ROOM_NAME");
        }
        if (roomName != null) {
            Toast.makeText(getApplicationContext(), "Joined " + roomName, Toast.LENGTH_LONG).show();
        } else {
            Log.e("RoomActivity", "room name was null");
        }

        networkServiceConnection = getNetworkServiceConnection();

        roomView = new RoomView(getApplicationContext());

        setContentView(R.layout.room_layout);
        ConstraintLayout roomLayout = (ConstraintLayout)findViewById(R.id.roomView_layout);
        roomLayout.addView(roomView);

        //For seekbar
        seekbar = findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        //For color picker
        color_picker_view = findViewById(R.id.colorPickerLayout);
        color_picker_view.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                RoomViewActionUtility.ChangeColorHex(envelope.getHexCode());
            }
        });

        Button toggle_drawing = (Button) findViewById(R.id.toggle_drawing);
        toggle_drawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = roomView.getDrawingStatus();
                if (status == 1) {
                    roomView.setDrawingStatus(0);
                }

                if (status == 0) {
                    roomView.setDrawingStatus(1);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                handleNetworkStarted();
            }

            @Override
            public void onServiceDisconnected (ComponentName name){

            }
        };
    }

    private void handleNetworkStarted() {
        roomView.setNetworkLayer(networkLayer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(networkServiceConnection);
    }

    public void showDocument(View view) {
        Intent intent = new Intent(this, DocumentActivity.class);
        startActivity(intent);
    }

    public void toggleToolbox(View view) {
        ConstraintLayout toolboxLayout = findViewById(R.id.toolbox_view);

        if (toolboxLayout.getVisibility() == View.VISIBLE) {
            toolboxLayout.setVisibility(View.GONE);
        }
        else {
            toolboxLayout.setVisibility(View.VISIBLE);
        }
    }

    public void toggleColorPickerView(View view) {
        ConstraintLayout colorPickerLayout = findViewById(R.id.color_picker_view);

        if (colorPickerLayout.getVisibility() == View.VISIBLE) {
            colorPickerLayout.setVisibility(View.GONE);
        }
        else {
            colorPickerLayout.setVisibility(View.VISIBLE);
        }
    }

    public void changeColor(View view) {
        RoomViewActionUtility.ChangeColorHex(view.getTag().toString());
    }

    public void saveCanvas(View view) {

    }

    // Used for the SeekBar to change pen width
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int seekbar_progress, boolean fromUser) {
            RoomViewActionUtility.ChangeWidth((float)seekbar_progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            RoomViewActionUtility.ChangeWidth((float)seekbar.getProgress());
        }
    };
}
