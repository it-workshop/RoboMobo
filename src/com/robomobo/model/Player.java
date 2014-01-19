package com.robomobo.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
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
        try
        {
            Paint player_paint = new Paint();
            player_paint.setStyle(Paint.Style.FILL);
            player_paint.setARGB(255, 0, 0, 255);
            can.drawCircle(m_pos.x, m_pos.y, 5, player_paint);
        }
        catch (Exception e)
        {
            can.restore();
            Log.e("Draw error", e.getMessage());
        }
    }
}
