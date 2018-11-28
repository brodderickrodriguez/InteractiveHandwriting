package group6.interactivehandwriting.activities.Room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import group6.interactivehandwriting.R;

import android.widget.SeekBar;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import group6.interactivehandwriting.activities.Room.views.DocumentView;
import group6.interactivehandwriting.activities.Room.views.RoomView;
import group6.interactivehandwriting.common.app.Permissions;
import group6.interactivehandwriting.common.network.NetworkLayer;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;

public class RoomActivity extends AppCompatActivity {
    private RoomView roomView;
    private SeekBar seekbar;
    private ColorPickerView color_picker_view;
    private Context context;
    private DocumentView documentView;
    private ConstraintLayout roomLayout;

    private boolean resizeToggle;

    NetworkLayer networkLayer;
    ServiceConnection networkServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        getRoomName(savedInstanceState);

        networkServiceConnection = getNetworkServiceConnection();

        setContentView(R.layout.room_layout);
        roomLayout = findViewById(R.id.roomView_layout);
        roomLayout.setBackgroundColor(Color.WHITE);

        roomView = new RoomView(context);
        roomLayout.addView(roomView);
        roomLayout.bringChildToFront(roomView);

        documentView = findViewById(R.id.documentView);

        seekbar = findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        resizeToggle = false;

        color_picker_view = findViewById(R.id.colorPickerLayout);
        color_picker_view.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                RoomViewActionUtility.ChangeColorHex(envelope.getHexCode());
            }
        });
    }

    private void getRoomName(Bundle savedInstanceState) {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkLayerService.startNetworkService(this);
        NetworkLayerService.bindNetworkService(this, networkServiceConnection);
        roomLayout.bringChildToFront(roomView);
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
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(Permissions.REQUEST_CODE_FILEPICKER)
                .withHiddenFiles(true)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("JAKE", "REQUEST CODE " + String.valueOf(requestCode));


        if (requestCode == Permissions.REQUEST_CODE_FILEPICKER) {
            Log.v("JAKE", "FILE RECIEVED");
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            showPDF(new File(filePath));
        }
    }

    public void resizeDoc(View view) {
        if (resizeToggle == false) {
            documentView.bringToFront();
            documentView.activateResizeMode();
            resizeToggle = true;
        } else {
            roomView.bringToFront();
            documentView.deactivateResizeMode();
            resizeToggle = false;
        }
    }

    public void showPDF(File file) {
        try {
            PdfiumCore pdfiumCore = new PdfiumCore(context);

            ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);

            int pageNum = 0;
            pdfiumCore.openPage(pdfDocument, pageNum);

            //Get current screen size
            DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
            int screen_width = metrics.widthPixels;
            int screen_height = metrics.heightPixels;

            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(screen_width, screen_height, Bitmap.Config.ARGB_8888);


            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0, screen_width, screen_height, true);

            documentView.setImageBitmap(bitmap); // TODO this functionality should be separate from the RoomView functionality

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch(IOException ex) {
            ex.printStackTrace();
        }
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

    public void undo(View view) {
        roomView.undo();
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

    public void colorErase(View view) {
        RoomViewActionUtility.setEraser();
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
