package group6.interactivehandwriting.common.app;

import java.util.Random;

/**
 * Created by JakeL on 9/22/18.
 */

public class Profile {
    private static boolean isNameSet = false;

    private String name;

    // TODO this needs to be fixed
    public String getDeviceName() {
        if (Profile.isNameSet == false) {
            name = "Device" + (new Random()).nextInt(10000);
            Profile.isNameSet = true;
        }
        return name;
    }
}
