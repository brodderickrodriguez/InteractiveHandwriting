package group6.interactivehandwriting;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;


public class DrawActivity extends Activity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw);

        drawingView = findViewById(R.id.drawing_view);
        drawingView.setDrawingColor(Color.RED);
    }

    public void setColorRed(View view) {
        drawingView.setDrawingColor(Color.RED);
    }

    public void setColorBlue(View view) {
        drawingView.setDrawingColor(Color.BLUE);
    }

}