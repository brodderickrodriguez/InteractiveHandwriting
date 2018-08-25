package group6.interactivehandwriting;

import android.os.Bundle;
import android.app.Activity;


public class DrawActivity extends Activity {

    DrawingView dv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
        setContentView(dv);
    } // onCreate()


} // DrawActivity()