package group6.interactivehandwriting.common.app;

import java.util.UUID;

import group6.interactivehandwriting.common.app.rooms.Room;

/**
 * Created by JakeL on 9/22/18.
 */

public class Profile {
    public long deviceId;
    public String username;

    public static final Long DEVICE_ID;

    static {
        DEVICE_ID = UUID.randomUUID().getLeastSignificantBits();
    }

    public Profile() {
        deviceId = 0;
        username = "";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (other instanceof Profile) {
            return ((Profile) other).deviceId == this.deviceId;
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return (int)deviceId;
    }
}
