package group6.interactivehandwriting.activities.Room;

import java.io.File;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.github.barteksc.pdfviewer.PDFView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;


import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.views.ImageUtilityView;
import group6.interactivehandwriting.common.actions.ModifyDocumentAction;

public class RoomActivity extends Activity {
    private String TAG = "RoomActivity: ";
    private static final String MIME_TYPE_TEXT = "application/pdf";

    /* Request/Persmission Codes */
    private static final int RC_SIGN_IN = 10;
    private static final int REQUEST_CODE_FILEPICKER = 20;
    private static final int RESULT_LOAD_IMAGE = 30;
    private static final int READ_EXTERNAL_STORAGE = 40;
    private static final int PERMISSIONS_REQUEST_CODE = 50;


    private View view;
    private ImageUtilityView img_view;
    private PDFView pdf_view;
    private ModifyDocumentAction documentAction;

    /* Google Drive Instance Variables */
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        view = findViewById(R.id.room_view);

        //Instantiate document action
        documentAction = new ModifyDocumentAction();
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (mGoogleSignInAccount != null && GoogleSignIn.hasPermissions(mGoogleSignInAccount, new Scope(Scopes.DRIVE_APPFOLDER))) {
            System.out.println("Grabbed last account");
        } else {
            System.out.println("Couldn't grab account");
        }
    }

    /**************** Google Drive API - CANT GET DRIVE TO LAUNCH****************/
    /**
     * Performs the complete Google Sign in.  Configures GoogleSignInOptions, retrieves client, starts sign in Intent,
     * and starts open file intent.
     */
    private void performGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = configureSignInOptions();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        startGoogleSignInIntent();
    }

    /**
     * Configures GoogleSignInOptions
     * @return GoogleSignInOptions gso
     */
    private GoogleSignInOptions configureSignInOptions() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return gso;
    }

    /**
     * Starts the Google sign in intent.
     */
    private void startGoogleSignInIntent() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Handles sign in results.  Assigns mGoogleSignInAccount to the account that the user signed in with.
     * @param completedTask Task to get the result of the operation.
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            mGoogleSignInAccount = completedTask.getResult(ApiException.class);
            System.out.println("Signed in successfully..");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * Sign out.
     */
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Sign out complete..");
                    }
                });
    }

    /********************** Image Display **************************/
    /**
     * Starts the show Intent to load a media file form storage.
     * Buggy - Do not use
     * TODO: Correctly grab file path from URI...
     */
    private void showImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    /************************ File Opener (Internal/External Storage) *****************************/
    /**
     * Request permission to read data from external storage.
     */
    private void requestPermissionExternalStorage() {
        ActivityCompat.requestPermissions(RoomActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_EXTERNAL_STORAGE);
    }


    /**
     * Opens storage to look for files
     * Might want to use this function for looking for images as well.
     */
    private void showPDF() {
        checkPermissions();
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_CODE_FILEPICKER)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    private void checkPermissions() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                System.out.println("Failure...");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            System.out.println("Success..");
        }
    }

    /*********************** Utility Functions *************************/
    /**
     * Shows the image view.
     */
    public void showImageView() {
        img_view = (ImageUtilityView) findViewById(R.id.img_view);
        img_view.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the image view.
     */
    public void hideImageView() {
        img_view = (ImageUtilityView) findViewById(R.id.img_view);
        img_view.setVisibility(View.GONE);
    }

    /**
     * Hides the PDF view.
     */
    public void hidePDFView(PDFView pView) {
        pdf_view = (PDFView) findViewById(R.id.pdfView);
        pdf_view = pView;
        pdf_view.setVisibility(View.GONE);
    }

    /**
     * Shows the PDF view.
     */
    public void showPDFView() {
        pdf_view = (PDFView) findViewById(R.id.pdfView);
        pdf_view.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the whiteboard
     */
    public void showWhiteBoard() {
        if (pdf_view != null & pdf_view.getVisibility() == View.VISIBLE) {
            pdf_view.setVisibility(View.INVISIBLE);
        }

        view = findViewById(R.id.room_view);
    }


    /***************************** Activity Functions *****************************/
    /**
     * Handles the results of any activites that occur during the application lifetime.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        File file = null;
        switch (requestCode) {
            case REQUEST_CODE_FILEPICKER: //Result from loading document from storage
                if (requestCode == REQUEST_CODE_FILEPICKER && resultCode == RESULT_OK) {
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    //hideImageView();
                    pdf_view = (PDFView) findViewById(R.id.pdfView);
                    documentAction.openDocumentWithFile(filePath, (PDFView) pdf_view);
                }
                break;
            case RESULT_LOAD_IMAGE: //Result from loading image from storage.
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    img_view.setBitmap(BitmapFactory.decodeFile(picturePath));
                    img_view = (ImageUtilityView) findViewById(R.id.img_view);

                    pdf_view = (PDFView) findViewById(R.id.pdfView);
                    hidePDFView(pdf_view);
                    showImageView();
                }
                break;
        }
    }

    /**
     * Handles any Permission request during the applications lifetime.
      * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied
                    Toast.makeText(RoomActivity.this, "Permission denied to read your External storage",
                                Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(RoomActivity.this, "Permission denied to read your External storage",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
