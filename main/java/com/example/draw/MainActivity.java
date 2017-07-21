package com.example.draw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CanvasView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvas = (CanvasView) findViewById(R.id.canvas);

    }

    //clear button clears canvas with canvas' clearCanvas() method
    public void clear(View view){
        canvas.clearCanvas();
    }
    public void red(View view){
        canvas.changeRed();
    }
    public void green(View view){
        canvas.changeGreen();
    }
    public void blue(View view){
        canvas.changeBlue();
    }
    public void black(View view){
        canvas.changeBlack();
    }


}
