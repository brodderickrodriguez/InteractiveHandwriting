package group6.interactivehandwriting.common.app.rooms;

import group6.interactivehandwriting.common.app.Profile;

/**
 * Created by JakeL on 10/25/18.
 */

public class Room {
//    private static final int MAX_USERS = 10;
    public static final int VOID_ROOM_NUMBER = 0;
    public String name;
    public long deviceId;
//    private int numUsers;
//    public Profile[] profileList = new Profile[MAX_USERS];

    public Room() {
        this(0, "");
    }

    public Room(long deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
//        this.numUsers = 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (other instanceof Room) {
            Room otherRoom = (Room) other;
            return otherRoom.getRoomNumber() == this.getRoomNumber();
        } else {
            return false;
        }
    }

    public boolean isValidRoom() {
        return deviceId != 0;
    }

    public int getRoomNumber() {
        return (int) deviceId + name.hashCode();
    }
//
//    public void addProfileToRoom(Profile profile) {
//        profileList[numUsers] = profile;
//        numUsers++;
//    }
}
