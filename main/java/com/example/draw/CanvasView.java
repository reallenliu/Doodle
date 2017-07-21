package com.example.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by allenliu on 2017-07-21.
 */

public class CanvasView extends View {



    //Bitmap to hold pixels
    private Bitmap mBitmap;
    //a drawing primitive
    private Path mPath;
    //a paint to hold colors and styles
    private Paint mPaint;
    //coordinate variables
    private float mX, mY;
    //distance that can be tolerated before drawing path
    private static final float TOLERANCE = 1f;
    Context context;

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //set context equal to parameter context
        this.context = context;

        mPath = new Path();
        mPaint = new Paint();

        //smooths the edges
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        //play with the styles to see what each does
        mPaint.setStyle(Paint.Style.STROKE);
        //sets how the stroke joins, play with this
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(20f);

    }

    //this draws the view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    public void changeRed(){
        mPaint.setColor(Color.RED);
        invalidate();
    }
    public void changeGreen(){
        mPaint.setColor(Color.GREEN);
        invalidate();
    }
    public void changeBlue(){
        mPaint.setColor(Color.BLUE);
        invalidate();
    }
    public void changeBlack(){
        mPaint.setColor(Color.BLACK);
        invalidate();
    }


    //calculates how big the view is
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    //the first touch sets the path equal to the coordinate
    private void startTouch(float x, float y){
        //where to start the path on the first touch
        mPath.moveTo(x,y);
        mX = x;
        mY = y;
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
    }

    //custom clearCanvas method
    public void clearCanvas(){
        mPath.reset();
        invalidate();
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
