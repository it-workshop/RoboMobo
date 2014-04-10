package com.robomobo.model;

import android.graphics.*;
import android.util.Log;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;
import com.robomobo.view.IconProvider;

/**
 * Created by Loredan on 08.12.13.
 */
public class Player implements IDrawable
{
    //NOTE: assuming OX goes to the right and OY goes up.

    protected PointF m_pos;
    protected int m_score;
    protected int m_id;

    public Player()
    {
        m_pos = new PointF();
    }

    public Player(float initX, float initY)
    {
        m_pos = new PointF(initX, initY);
    }

    public float getX()
    {
        return m_pos.x;
    }

    public float getY()
    {
        return m_pos.y;
    }

    public PointF getPos()
    {
        return new PointF(m_pos.x, m_pos.y);
    }

    public void move(float x, float y)
    {
        m_pos.set(x, y);
        //Log.wtf("player","x: "+posX+", y: "+posY);
    }

    public void move(PointF pos)
    {
        m_pos = pos;
    }

    public int getScore()
    {
        return m_score;
    }

    public void addScore(int score)
    {
        m_score += score;
    }

    @Override
    public void draw(Canvas can, long time)
    {
        can.save();
        {

            can.translate(this.m_pos.x * GRAPHICS.scale, this.m_pos.y * GRAPHICS.scale);

            Paint p = new Paint();
            p.setColorFilter(new LightingColorFilter(Color.GREEN, 0));
            p.setColor(Color.GREEN);

            if(GameActivity.DEBUG) can.drawCircle(0, 0, 3, p);
            else
            {
                Bitmap b = IconProvider.getIconBitmap("player", 0);
                can.drawBitmap(b, new Rect(0, 0, b.getWidth(), b.getHeight()), new RectF(-16, -16, 16, 16), p);
            }
        }
        can.restore();
    }

    public void moveRelative(float x, float y)
    {
        this.move(this.m_pos.x + x, this.m_pos.y + y);
    }
}
