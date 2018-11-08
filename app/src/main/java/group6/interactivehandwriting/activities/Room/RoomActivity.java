package group6.interactivehandwriting.activities.Room;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.common.app.actions.file.ModifyDocumentAction;

import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.views.RoomView;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkLayer;
import group6.interactivehandwriting.common.network.NetworkLayerBinder;
import group6.interactivehandwriting.common.network.NetworkLayerService;

// TODO move the file manipulation stuff to its own class
public class RoomActivity extends Activity {
    public static final int REQUEST_CODE_FILEPICKER = 2;

    private PDFView pdf_view;
    private ModifyDocumentAction documentAction;
    private View view;
    private RelativeLayout main_view;

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
        Context context = this.getApplicationContext();
        Profile profile = networkLayer.getMyProfile();
        documentAction = new ModifyDocumentAction(context, profile, networkLayer);
        View view = new RoomView(context, profile, networkLayer);
        main_view = findViewById(R.id.main_layout);
        setContentView(R.layout.room_layout);

        ConstraintLayout roomLayout = (ConstraintLayout)findViewById(R.id.roomView_layout);
        roomLayout.addView(view);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(networkServiceConnection);
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

    // A few hard coded colors (Will eventually switch this to a single function where
    // the parameters can be altered in the layout
    public void colorRed(View view) {
        RoomViewActionUtility.ChangeColorCustom(255, 0, 0);
    }

    public void colorGreen(View view) {
        RoomViewActionUtility.ChangeColorCustom(0, 255, 0);
    }

    public void colorBlue(View view) {
        RoomViewActionUtility.ChangeColorCustom(0, 0, 255);
    }

    public void colorErase(View view) {
        RoomViewActionUtility.toggleEraser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (isFilePickerResult(requestCode)) {
            if (resultCode == RESULT_OK) {
                handleFilePickerResult(data);
            }
        }
    }

    private boolean isFilePickerResult(int requestCode) {
        return requestCode == REQUEST_CODE_FILEPICKER;
    }

    private void handleFilePickerResult(Intent fileSelectionIntent) {
        String filePath = fileSelectionIntent.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
        File file = new File(filePath);
        pdf_view = findViewById(R.id.pdf_view);
        documentAction.openDocumentWithFile(file, pdf_view);
    }

    public void showPDF(View view) {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_CODE_FILEPICKER)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    public void hidePDFView(PDFView pView) {
        pdf_view = (PDFView) findViewById(R.id.pdf_view);
        pdf_view = pView;
        pdf_view.setVisibility(View.GONE);
    }

    public void showPDFView(View view) {
        pdf_view = (PDFView) findViewById(R.id.pdf_view);
        pdf_view.setVisibility(View.VISIBLE);
    }

    public void showWhiteBoard(View view) {
        if (pdf_view != null & pdf_view.getVisibility() == View.VISIBLE) {
            pdf_view.setVisibility(View.INVISIBLE);
        }
        setContentView(view);
    }
}
