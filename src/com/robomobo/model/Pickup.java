package com.robomobo.model;

import android.graphics.*;
import android.os.CountDownTimer;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;
import com.robomobo.view.IconProvider;

/**
 * Created by Vsevolod on 17.03.14.
 */
public class Pickup implements IDrawable
{
    public PickupType m_type = PickupType.none;
    private int m_lifetime_ms; //lifetime in milliseconds
    private PointF m_coords;
    private Map m_mapReference;
    public int mId;

    public Pickup(float x, float y, int id)
    {
        m_coords = new PointF(x, y);
        mId = id;
    }

    public Pickup(float x, float y, int id, PickupType type)
    {
        this(x, y, id);
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
            Bitmap b = IconProvider.getIconBitmap(m_type.m_iconId, m_lifetime_ms);
            int f = 0;
            switch(m_type)
            {                                                              //Left here for special fancy rendering cases.
                /*case RoundYellowThingyThatLooksLikeSun:

                    f = (int)((m_lifetime_ms / GRAPHICS.PICKUP_0_FRAMERATE) % GRAPHICS.PICKUP_0.size());

                    can.drawBitmap(GRAPHICS.PICKUP_0.get(f), m_coords.x*GRAPHICS.scale-GRAPHICS.PICKUP_0.get(f).getWidth()/2, m_coords.y*GRAPHICS.scale-GRAPHICS.PICKUP_0.get(f).getHeight()/2, p);
                    break;

                case BlueIcyCrystalStuff:
                    f = (int)((m_lifetime_ms / GRAPHICS.PICKUP_1_FRAMERATE) % GRAPHICS.PICKUP_1.size());
                    can.drawBitmap(GRAPHICS.PICKUP_1.get(f), m_coords.x*GRAPHICS.scale-GRAPHICS.PICKUP_1.get(f).getWidth()/2, m_coords.y*GRAPHICS.scale-GRAPHICS.PICKUP_1.get(f).getHeight()/2, p);
                    break; */

                default:
                    if(b == null) can.drawCircle(m_coords.x*GRAPHICS.scale, m_coords.y*GRAPHICS.scale, 3, p);
                    else can.drawBitmap(b, m_coords.x*GRAPHICS.scale-b.getWidth()/2, m_coords.y*GRAPHICS.scale-b.getHeight()/2, p);
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
                onRemoved();
            }
        }.start();
    }

    public PointF getPosition()
    {
        return this.m_coords;
    }

    public void onPickedUp()
    {
        //Increase da score.
        onRemoved();
    }

    public void onRemoved()
    {
        m_mapReference.m_drawables.remove(this);
        m_mapReference.m_pickups.remove(this);
    }

    public static enum  PickupType
    {
        none(1, ""),
        RoundYellowThingyThatLooksLikeSun(10, "pickup_0"),                   //Yes, such long type names are necessary. NECESSARY I SAY!
        BlueIcyCrystalStuff(20, "pickup_1");

        public int m_points;
        public String m_iconId;

        private PickupType(int p, String s)
        {
            m_points = p;
            m_iconId = s;
        }
    }
}
