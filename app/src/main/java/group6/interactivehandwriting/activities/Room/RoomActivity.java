package group6.interactivehandwriting.activities.Room;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.nearby.connection.Payload;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.actions.ModifyDocumentAction;

import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.activities.Room.views.RoomView;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkManager;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkManager;

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
    private static final int REQUEST_CODE_FILEPICKER = 2;

    private PDFView pdf_view;
    private ModifyDocumentAction documentAction;
    private View view;
    private RelativeLayout main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this.getApplicationContext();
        Profile profile = new Profile();
        NetworkManager networkManager = new NCNetworkManager(context, profile);
        documentAction = new ModifyDocumentAction(context, profile, networkManager);
        view = new RoomView(context, profile, networkManager);
        main_view = (RelativeLayout) findViewById(R.id.main_layout);

        setContentView(view);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
           ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        File file = null;
        switch(requestCode) {
            case REQUEST_CODE_FILEPICKER:
                if (requestCode == REQUEST_CODE_FILEPICKER && resultCode == RESULT_OK) {
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    file = new File(filePath);


                    setContentView(R.layout.main);

                    pdf_view = (PDFView) findViewById(R.id.pdf_view);
                    documentAction.openDocumentWithFile(file, (PDFView) pdf_view);

                    /**** setContentView(view) to return to whiteboard ****/
                }
             break;
        }
    }
    /**
     * Opens storage to look for files
     */
    private void showPDF() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_CODE_FILEPICKER)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    /**
     * Hides the PDF view.
     */
    public void hidePDFView(PDFView pView) {
        pdf_view = (PDFView) findViewById(R.id.pdf_view);
        pdf_view = pView;
        pdf_view.setVisibility(View.GONE);
    }

    /**
     * Shows the PDF view.
     */
    public void showPDFView() {
        pdf_view = (PDFView) findViewById(R.id.pdf_view);
        pdf_view.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the whiteboard
     */
    public void showWhiteBoard() {
        if (pdf_view != null & pdf_view.getVisibility() == View.VISIBLE) {
            pdf_view.setVisibility(View.INVISIBLE);
        }

        setContentView(view);
    }
}
