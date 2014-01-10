package com.robomobo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Roman on 10.01.14.
 */
public class SurfaceViewIngame extends SurfaceView implements SurfaceHolder.Callback
{
    private ThreadDrawIngame drawThread;

    public SurfaceViewIngame(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        drawThread = new ThreadDrawIngame(getHolder(), getResources());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry)
        {
            try
            {
                drawThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {

            }
        }
    }
}
