package group6.interactivehandwriting.activities.Room;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.renderscript.ScriptGroup;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.DocumentFile;
import android.util.DisplayMetrics;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.views.DocumentView;
import group6.interactivehandwriting.common.network.NetworkLayer;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;


public class DocumentActivity extends Activity {
    private Context context;
    private DocumentView view;


    /* Request/Persmission Codes */
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private static final int REQUEST_CODE_FILEPICKER = 2;

    private SeekBar seekbar;
    private ColorPickerView color_picker_view;

    NetworkLayer networkLayer;
    ServiceConnection networkServiceConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            loadPDF();
        } finally {
            networkServiceConnection = getNetworkServiceConnection();

            context = this.getApplicationContext();
            view = new DocumentView(context);

            setContentView(R.layout.room_layout);

            ConstraintLayout roomLayout = (ConstraintLayout)findViewById(R.id.docView_layout);
            roomLayout.addView(view);
        }

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

        Button return_home = (Button) findViewById(R.id.return_home);
        return_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity thisActivitiy = getDocActivity();
                thisActivitiy.finish();
            }
        });

        Button toggle_drawing = (Button) findViewById(R.id.toggle_drawing);
        toggle_drawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = view.getDrawingStatus();
                if (status == 1) {
                    view.setDrawingStatus(0);
                }

                if (status == 0) {
                    view.setDrawingStatus(1);
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

    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void handleNetworkStarted() {
        view.setNetworkLayer(networkLayer);
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

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(networkServiceConnection);
    }

    /** Handles user acceptance (or denial) of our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                finish();
                return;
            }
        }
        recreate();
    }

    public void showPDF(File file) {
        ParcelFileDescriptor fd = null;
        Bitmap bitmap = null;
        PdfDocument pdfDocument = null;
        PdfiumCore pdfiumCore = null;
        int pageNum = 0;
        pdfiumCore = new PdfiumCore(context);
        try {
            fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfDocument = pdfiumCore.newDocument(fd);

            pdfiumCore.openPage(pdfDocument, pageNum);

           //int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
           // int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);

            //Get current screen size
            DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
            int screen_width = metrics.widthPixels;
            int screen_height = metrics.heightPixels;

            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            bitmap = Bitmap.createBitmap(screen_width, screen_height,
                    Bitmap.Config.ARGB_8888);


            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    screen_width, screen_height, true);

            view.setImageBitmap(bitmap);

            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("SLeeping..");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        File file = null;
        switch(requestCode) {
            case REQUEST_CODE_FILEPICKER:
                if (requestCode == REQUEST_CODE_FILEPICKER) {
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    file = new File(filePath);

                    showPDF(file);

                }
                break;
        }
    }
    /**
     * Opens storage to look for files
     */
    public void loadPDF() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_CODE_FILEPICKER)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
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

    public Activity getDocActivity() {
        return this;
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
