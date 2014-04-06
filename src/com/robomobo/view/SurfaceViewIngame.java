package com.robomobo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.robomobo.model.GameActivity;
import com.robomobo.model.Map;
import com.robomobo.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Roman on 10.01.14.
 */
public class SurfaceViewIngame extends SurfaceView implements SurfaceHolder.Callback
{
    private ThreadDrawIngame m_drawThread;
    private Map m_currentMap;
    private HashMap<String, Player> m_players;
    public float m_scale;

    public SurfaceViewIngame(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getHolder().addCallback(this);

        m_currentMap = ((GameActivity) context).m_currentMap;
        m_players = ((GameActivity) context).m_players;
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        m_currentMap.draw(canvas, 0);
        for(Player player : m_players.values())
            player.draw(canvas, 0);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        float scaleX = (width-1)/m_currentMap.getWidth();
        float scaleY = (height-1)/m_currentMap.getHeight();
        GRAPHICS.scale = (scaleX < scaleY) ? scaleX : scaleY;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        float scaleX = (getWidth()-1)/m_currentMap.getWidth();
        float scaleY = (getHeight()-1)/m_currentMap.getHeight();
        GRAPHICS.scale = scaleX < scaleY ? scaleX : scaleY;
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
                catch (Exception e)
                {
                    Log.e("error", "Achtung, bitte, bitte! Draw thread threw an exception!");
                    Log.e("error", Log.getStackTraceString(e));
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
