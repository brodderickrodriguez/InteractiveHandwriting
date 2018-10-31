package group6.interactivehandwriting.common.app.rooms;

/**
 * Created by JakeL on 10/25/18.
 */

public class Room {
    public static final int VOID_ROOM_NUMBER = 0;
    public String name;
    public long deviceId;

    public Room() {
        this(0, "");
    }

    public Room(long deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
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
}
