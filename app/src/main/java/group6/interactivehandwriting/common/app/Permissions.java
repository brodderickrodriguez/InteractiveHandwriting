package group6.interactivehandwriting.common.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by JakeL on 10/17/18.
 */

public class Permissions {
    private static final String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    public static final int REQUEST_CODE_FILEPICKER = 2;

    public static void requestPermissions(Activity activity) {
        if (!hasPermissions(activity.getApplicationContext(), REQUIRED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
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
}
