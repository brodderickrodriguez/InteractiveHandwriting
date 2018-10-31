package group6.interactivehandwriting.activities.Menu.adapaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.Set;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.common.app.rooms.Room;

/**
 * Created by JakeL on 10/30/18.
 */

public class RoomAdapter extends ArrayAdapter<Room> {
    private Context context;

    public RoomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View button = convertView;
        if (button == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            button = inflater.inflate(R.layout.room_button, parent, false);
        }

        Room room = getItem(position);

        TextView roomName = button.findViewById(R.id.room_item);
        roomName.setText(room.name);

        return button;
    }

    @Override
    public void addAll(Collection<? extends Room> rooms) {
        for (Room room : rooms) {
            if (!contains(room)) {
                add(room);
            }
        }

        notifyDataSetInvalidated();
    }

    private boolean contains(Room room) {
        for (int i = 0; i < getCount(); i++) {
            if (room.equals(getItem(i))) {
                return true;
            }
        }
        return false;
    }
}
