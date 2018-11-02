package group6.interactivehandwriting.activities.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.views.DocumentView;
import group6.interactivehandwriting.activities.Room.views.RoomView;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkManager;
import group6.interactivehandwriting.common.network.nearby.connections.NCNetworkManager;

//TODO Add toolbar on top of the document view and add button to enable/disable drawing for resizing

public class DocumentActivity extends Activity {
    private DocumentView view;
    private Context context;

    private ScaleGestureDetector mScaleGestureDetector;

    private float mScaleFactor = 1.0f;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        Profile profile = new Profile();
        NetworkManager networkManager = new NCNetworkManager(context, profile);

        view = new DocumentView(context, profile, networkManager);
        setContentView(view);

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        loadPDF();


        // Adds the RoomView to the layout and inflates it
       // ConstraintLayout roomLayout = (ConstraintLayout)findViewById(R.id.roomView_layout);
       // roomLayout.addView(view);
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
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            view.setImageBitmap(bitmap);

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
                if (requestCode == REQUEST_CODE_FILEPICKER && resultCode == RESULT_OK) {
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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){

            mScaleFactor *= scaleGestureDetector.getScaleFactor();

            mScaleFactor = Math.max(0.1f,

                    Math.min(mScaleFactor, 10.0f));

            view.setScaleX(mScaleFactor);

            view.setScaleY(mScaleFactor);

            return true;

        }
    }
}
