package com.robomobo.model;

import android.graphics.*;
import android.util.Log;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;

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

    public void changePos(float x, float y)
    {
        m_pos.set(x, y);
        //Log.wtf("player","x: "+posX+", y: "+posY);
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

            can.scale(GRAPHICS.scale, GRAPHICS.scale);
            can.translate(this.m_pos.x, this.m_pos.y);

            Paint p = new Paint();
            p.setColorFilter(new LightingColorFilter(Color.GREEN, 0));

            if(GameActivity.DEBUG) can.drawCircle(0, 0, 0.5f, p);
            else
            can.drawBitmap(GRAPHICS.PLAYER, new Rect(0, 0, 32, 32), new RectF(-2, -2, 2, 2), p);
        }
        can.restore();
    }
}
