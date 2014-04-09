package com.robomobo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Roman on 06.04.14.
 */
public class ViewJoystick extends View
{
    private int direction = 0;
    private float strength = 0;

    public ViewJoystick(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ViewJoystick(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint p = new Paint();

        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(this.getWidth() / 2f, this.getWidth() / 2f, getWidth() / 2f, p);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(this.getWidth() / 2f, this.getWidth() / 2f, getWidth() / 2f, p);

        canvas.save();
        {
            canvas.translate(this.getWidth() / 2f, this.getWidth() / 2f);
            canvas.rotate(direction);
            canvas.translate(strength, 0);

            p.setColor(Color.GRAY);
            p.setStyle(Paint.Style.FILL);
            canvas.drawCircle(0, 0, getWidth() / 4f, p);
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(0, 0, getWidth() / 4f, p);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent moe)
    {
        if(moe.getAction() == MotionEvent.ACTION_DOWN || moe.getAction() == MotionEvent.ACTION_MOVE)
        {
            double d = Math.atan((moe.getX() - (this.getWidth() / 2f))/(moe.getY() - (this.getHeight() / 2f)));
            if((moe.getY() - (this.getHeight() / 2f)) < 0) d += Math.PI;
            this.direction = (int) Math.toDegrees(d * -1) + 90;

            float f = (float) Math.sqrt(Math.pow(moe.getX() - (this.getWidth() / 2f), 2) + Math.pow(moe.getY() - (this.getHeight() / 2f), 2));
            this.strength = Math.min(f, ((this.getWidth() + this.getHeight()) / 8));
            this.invalidate();
        }

        if(moe.getAction() == MotionEvent.ACTION_UP)
        {
            this.strength = 0;
            this.direction = 0;
            this.invalidate();
        }
        return true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

    }

    @Override
    public void onMeasure(int w, int h)
    {
        w = Math.max(w, h);
        h = Math.max(w, h);
        setMeasuredDimension(w, h);
    }

    @Override
    protected int getSuggestedMinimumWidth()
    {
        return 100;
    }

    @Override
    protected int getSuggestedMinimumHeight()
    {
        return 100;
    }

}
