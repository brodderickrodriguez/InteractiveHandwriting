package group6.interactivehandwriting.activities.Room;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.nearby.connection.Payload;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
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
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this.getApplicationContext();
        Profile profile = new Profile();
        NetworkManager networkManager = new NCNetworkManager(context, profile);
        View view = new RoomView(context, profile, networkManager);
        setContentView(R.layout.room_layout);
        // Adds the RoomView to the layout and inflates it
        ConstraintLayout roomLayout = (ConstraintLayout)findViewById(R.id.roomView_layout);
        roomLayout.addView(view);
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
}
