package com.robomobo.model;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import com.robomobo.model.IdGenerator;
import com.robomobo.model.Map;
import com.robomobo.view.GRAPHICS;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class LocalPlayer extends Player
{
    private double m_direction;

    private boolean m_wallHit;
    public float[] m_wallHitPos;

    // probably we will use this constant somewhere else. Consider having a separate class for that
    public static final float WALL_UNHIT_RANGE = 5.0f;

    public LocalPlayer(float initX, float initY)
    {
        m_id = IdGenerator.getInstance().generatePlayerId();
        m_pos = new PointF(initX, initY);
        m_direction = 0;
        m_score = 0;
        m_wallHit = false;
        m_wallHitPos = new float[2];
        m_wallHitPos[0] = 0;
        m_wallHitPos[1] = 0;
    }

    public void move(float x, float y, Map map)
    {
        float[] direction = new float[2];
        float distance = (float) Math.sqrt(Math.pow(x - m_pos.x, 2) + Math.pow(y - m_pos.y, 2));
        direction[0] = (float) ((x - m_pos.x) / distance);
        direction[1] = (float) ((y - m_pos.y) / distance);
        m_direction = direction[1] >= 0 ? Math.acos(direction[0]) : 2 * Math.PI - Math.acos(direction[0]);

        if (!m_wallHit)
        {
            for (Map.Obstacle obstacle : map.m_obstacles)
            {
                if (obstacle.check(x, y))
                {

                    m_wallHitPos = obstacle.boundariesCrossing(m_pos.x, m_pos.x, x, y);
                    m_wallHit = true;
                    break;
                }
            }
        }
        else
        {
            if (Math.sqrt((x - m_wallHitPos[0]) * (x - m_wallHitPos[0]) + (y - m_wallHitPos[1]) * (y - m_wallHitPos[1])) < WALL_UNHIT_RANGE)
            {
                boolean stillInsideTheWall = false;
                for (Map.Obstacle obstacle : map.m_obstacles)
                {
                    if (obstacle.check(x, y))
                        stillInsideTheWall = true;
                }
                if (!stillInsideTheWall)
                    m_wallHit = false;
            }
        }
        m_pos.set(x, y);
    }

    @Override
    public void draw(Canvas can, long time)
    {
        can.save();
        try
        {
            Matrix transformMatrix = new Matrix();
            transformMatrix.postRotate((float) m_direction);
            transformMatrix.postTranslate(m_pos.x-GRAPHICS.PLAYER.getWidth()/2, m_pos.y-GRAPHICS.PLAYER.getHeight()/2);
            can.drawBitmap(GRAPHICS.PLAYER, transformMatrix, new Paint());
        }
        catch (Exception e)
        {
            can.restore();
            Log.e("Draw error", e.getMessage());
        }
    }
}
