package com.example.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allenliu on 2017-07-21.
 */

public class CanvasView extends View {



    //Bitmap to hold pixels
    private Bitmap mBitmap;
    //a drawing primitive
    private Path mPath;
    //a paint to hold colors and styles
    private Paint mPaint = new Paint();
    //coordinate variables
    private float mX, mY;
    //distance that can be tolerated before drawing path
    private static final float TOLERANCE = 1f;
    Context context;

    List<Pair<Path, Paint>> paths = new ArrayList<Pair<Path,Paint>>();

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //set context equal to parameter context
        this.context = context;

    }


    //this draws the view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Pair<Path,Paint> path_clr: paths){
            canvas.drawPath(path_clr.first, path_clr.second);
        }

    }



    //calculates how big the view is
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }



    //right now every starttouch makes a new path but there is also a new mPaint statement
    // in it too. When you select a changeColor, it changes the color of the current path, not the new path.
    // What needs to happen is when you select the color, the color of the new path changes. Remove the new Paint
    // statement in the starttouch so there isn't a black line every time, keep the style in it, don't have a setcolor.
    // In each color selector, first set the path as new and then set the color. Style will be assigned on touch.

    //right now first touch doesn't have a path unless you define a new path
    private void startTouch(float x, float y){
        //where to start the path on the first touch
        mPath = new Path();
        //smooths the edges
        mPaint.setAntiAlias(true);
        //play with the styles to see what each does
        mPaint.setStyle(Paint.Style.STROKE);
        //sets how the stroke joins, play with this
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(20f);

        mPath.moveTo(x,y);
        mX = x;
        mY = y;
        paths.add(Pair.create(mPath,mPaint));
    }



                            //current touch position
    private void moveTouch(float x, float y){
            //distance between start touch and movetouch position
        float dx = Math.abs(x-mX);
        float dy = Math.abs(y-mY);
        //if the distance is greater than tolerance distance
        if(dx > TOLERANCE || dy> TOLERANCE){
                     //moves mpath to the new point using quadratic formula to draw the curve
            mPath.quadTo(mX, mY, x, y);
            //the position is updated to current position
            mX = x;
            mY = y;

        }

    }

    //when you release touch, mpath goes to you the last touch position
    public void upTouch(){
        mPath.lineTo(mX,mY);
       // mPaint = new Paint();
    }

    //custom clearCanvas method
    public void clearCanvas(){
        for(Pair<Path,Paint> path_clr: paths){
            path_clr.first.reset();
        }
        invalidate();
    }

    public void changeRed(){
        newPaint();
        mPaint.setColor(Color.RED);
        invalidate();
    }
    public void changeGreen(){
        newPaint();
        mPaint.setColor(Color.GREEN);
        invalidate();
    }
    public void changeBlue(){
        newPaint();
        mPaint.setColor(Color.BLUE);
        invalidate();
    }
    public void changeBlack(){
        newPaint();
        mPaint.setColor(Color.BLACK);
        invalidate();
    }

    public void newPaint(){
        mPaint = new Paint();
    }



    //this is everything that happens when you touch the screen.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //get X coordinate of touch
        float x = event.getX();
        //get Y coordinate of touch
        float y = event.getY();

        //you could leave only action move invalidate and it would still appear to behave the same
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                //mPath does not show up until you release
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }

        return true;
    }
}
