package group6.interactivehandwriting.activities.Room;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import group6.interactivehandwriting.activities.Room.views.RoomView;

public class RoomActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = new RoomView(this);
        setContentView(view);
    }
}
