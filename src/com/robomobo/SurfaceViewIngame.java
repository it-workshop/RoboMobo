package com.robomobo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Roman on 10.01.14.
 */
public class SurfaceViewIngame extends SurfaceView implements SurfaceHolder.Callback
{
    private ThreadDrawIngame m_drawThread;

    public SurfaceViewIngame(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public void draw(Canvas canvas)
    {

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        m_drawThread = new ThreadDrawIngame(getHolder(), getResources(), this);
        m_drawThread.setRunning(true);
        m_drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        m_drawThread.setRunning(false);
        while (retry)
        {
            try
            {
                m_drawThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {

            }
        }
    }

    /**
     * Created by Roman on 10.01.14.
     */
    public static class ThreadDrawIngame extends Thread
    {
        private boolean m_runFlag = false;
        private SurfaceHolder m_surfaceHolder;
        private long m_prevTime;
        private SurfaceViewIngame m_surfaceViewIngame;

        public ThreadDrawIngame(SurfaceHolder surfaceHolder, Resources resources, SurfaceViewIngame surfaceViewIngame)
        {
            this.m_surfaceHolder = surfaceHolder;
            this.m_surfaceViewIngame = surfaceViewIngame;
            this.m_prevTime = System.currentTimeMillis();

            //make resource refs
        }

        public void setRunning(boolean run)
        {
            m_runFlag = run;
        }

        @Override
        public void run()
        {
            Canvas canvas;
            while (m_runFlag)
            {
                long now = System.currentTimeMillis();
                long elapsedTime = now - m_prevTime;
                if (elapsedTime > 30)
                {
                    m_prevTime = now;
                }
                canvas = null;
                try
                {
                    canvas = m_surfaceHolder.lockCanvas(null);
                    synchronized (m_surfaceHolder)
                    {
                        canvas.drawColor(Color.BLACK);

                        this.m_surfaceViewIngame.draw(canvas);
                    }
                }
                finally
                {
                    if (canvas != null)
                    {
                        m_surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
