package com.robomobo.model;

import android.graphics.*;
import android.os.CountDownTimer;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Всеволод on 17.03.14.
 */
public class Pickup implements IDrawable
{
    private ArrayList<Bitmap> m_icon;
    private int m_currentBitmap;
    private PointF m_coords;
    private List<Pickup> m_pickups;
    private List<IDrawable> m_drawables;

    public Pickup(float x, float y)
    {
        m_icon = GRAPHICS.PICKUP_0;
        m_coords = new PointF(x, y);
    }

    public Pickup(int type, float x, float y)
    {
        switch (type)
        {
            case 0:
                m_icon = GRAPHICS.PICKUP_0;
                break;
        }
        m_coords = new PointF(x, y);
    }

    @Override
    public void draw(Canvas can, long time)
    {
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        if(GameActivity.DEBUG)
            can.drawCircle(m_coords.x*GRAPHICS.scale, m_coords.y*GRAPHICS.scale, 3, p);
        else
        {
            can.drawBitmap(m_icon.get(m_currentBitmap), m_coords.x*GRAPHICS.scale-m_icon.get(m_currentBitmap).getWidth()/2, m_coords.y*GRAPHICS.scale-m_icon.get(m_currentBitmap).getHeight()/2, p);
        }
    }

    public void register(List<Pickup> pickups, List<IDrawable> drawables)
    {
        m_pickups = pickups;
        m_drawables = drawables;
        new CountDownTimer(20000, 100)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                m_currentBitmap = ++m_currentBitmap%25;
            }

            @Override
            public void onFinish()
            {
                m_drawables.remove(this);
                m_pickups.remove(this);
            }
        }.start();
    }
}
