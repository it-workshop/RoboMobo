package com.robomobo.model;

import android.graphics.*;
import android.os.CountDownTimer;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vsevolod on 17.03.14.
 */
public class Pickup implements IDrawable
{
    public PickupType m_type = PickupType.none;
    private long m_lifetime_ms; //lifetime in milliseconds
    private PointF m_coords;
    private Map m_mapReference;

    public Pickup(float x, float y)
    {
        m_coords = new PointF(x, y);
    }

    public Pickup(float x, float y, PickupType type)
    {
        this(x, y);
        m_type = type;
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
            int f = 0;
            switch(m_type)
            {
                case RoundYellowThingyThatLooksLikeSun:
                    f = (int)((m_lifetime_ms / GRAPHICS.PICKUP_0_FRAMERATE) % GRAPHICS.PICKUP_0.size());
                    can.drawBitmap(GRAPHICS.PICKUP_0.get(f), m_coords.x*GRAPHICS.scale-GRAPHICS.PICKUP_0.get(f).getWidth()/2, m_coords.y*GRAPHICS.scale-GRAPHICS.PICKUP_0.get(f).getHeight()/2, p);
                    break;

                case BlueIcyCrystalStuff:
                    f = (int)((m_lifetime_ms / GRAPHICS.PICKUP_1_FRAMERATE) % GRAPHICS.PICKUP_1.size());
                    can.drawBitmap(GRAPHICS.PICKUP_1.get(f), m_coords.x*GRAPHICS.scale-GRAPHICS.PICKUP_1.get(f).getWidth()/2, m_coords.y*GRAPHICS.scale-GRAPHICS.PICKUP_1.get(f).getHeight()/2, p);
                    break;

                default:
                    can.drawCircle(m_coords.x*GRAPHICS.scale, m_coords.y*GRAPHICS.scale, 3, p);
                    break;
            }

        }
    }

    public void register(Map map)
    {
        m_mapReference = map;

        new CountDownTimer(20000, 1)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                m_lifetime_ms++;
            }

            @Override
            public void onFinish()
            {
                onTimerExpired();
            }
        }.start();
    }

    public void onTimerExpired()
    {
        m_mapReference.m_drawables.remove(this);
        m_mapReference.m_pickups.remove(this);
    }

    public static enum  PickupType
    {
        none(1),
        RoundYellowThingyThatLooksLikeSun(10),                   //Yes, such long type names are necessary. NECESSARY I SAY!
        BlueIcyCrystalStuff(20);

        public int m_points;

        private PickupType(int p)
        {
            m_points = p;
        }
    }
}
