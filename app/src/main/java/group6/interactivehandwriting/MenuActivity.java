package group6.interactivehandwriting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import group6.interactivehandwriting.R;

public class MenuActivity extends AppCompatActivity {

    private Button roomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        setupResources();

    }

    private void setupResources() {
    }

    public void enterRoom(View view) {
        Intent drawingActivity = new Intent(this, DrawActivity.class);
        startActivity(drawingActivity);
    }

}
