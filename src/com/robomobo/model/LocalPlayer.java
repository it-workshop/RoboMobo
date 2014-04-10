package com.robomobo.model;

import android.graphics.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.robomobo.multiplayer.Networking;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IconProvider;

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

    public static final float PICKUP_PICK_UP_RANGE = 3.0f;

    public LocalPlayer(float initX, float initY, Networking networking)
    {
        m_id = IdGenerator.getInstance().generatePlayerId();
        m_pos = new PointF(initX, initY);
        m_direction = 0;
        m_score = 0;
        m_wallHit = false;
        m_wallHitPos = new float[2];
        m_wallHitPos[0] = 0;
        m_wallHitPos[1] = 0;

        networking.moveSelf(initX, initY);
    }

    public void move(float x, float y, Map map, Networking networking)
    {
        float[] direction = new float[2];
        float distance = (float) Math.sqrt(Math.pow(x - m_pos.x, 2) + Math.pow(y - m_pos.y, 2));
        direction[0] = (float) ((x - m_pos.x) / distance);
        direction[1] = (float) ((y - m_pos.y) / distance);
        m_direction = direction[0] > 0 ? Math.acos(-direction[1]) * 180 / Math.PI : 360 - Math.acos(-direction[1]) * 180 / Math.PI;

        if (!m_wallHit)
        {
            for (Map.Obstacle obstacle : map.m_obstacles)
            {
                if (obstacle.check(x, y))
                {

                    m_wallHitPos = obstacle.boundariesCrossing(m_pos.x, m_pos.y, x, y);
                    m_wallHit = true;
                    break;
                }
            }


            //for(Pickup pickup : map.m_pickups)
            for(int i = 0; i < map.m_pickups.size(); i++)
            {
                Pickup pickup = map.m_pickups.get(i);
                if(Math.pow(pickup.getPosition().x - x, 2.0d) + Math.pow(pickup.getPosition().y - y, 2.0d) < PICKUP_PICK_UP_RANGE)
                {
                    pickup.onPickedUp();
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
                    if (obstacle.check(x, y)) stillInsideTheWall = true;
                }
                if (!stillInsideTheWall) m_wallHit = false;
            }
        }
        m_pos.set(x, y);

        if(m_wallHit)
            networking.moveSelf(m_wallHitPos[0], m_wallHitPos[1]);
        else
            networking.moveSelf(x, y);
    }
    
    public void move(PointF pos, Map map, Networking networking)
    {
        move(pos.x, pos.y, map, networking);
    }

    public void moveRelative(float x, float y, Map map, Networking networking)
    {
        this.move(this.m_pos.x + x, this.m_pos.y + y, map, networking);
    }

    @Override
    public void draw(Canvas can, long time)
    {
        if (m_wallHit)
        {
            can.save();
            {
                Paint p = new Paint();
                p.setStyle(Paint.Style.FILL);
                p.setARGB(127, 255, 0, 0);

                can.translate(m_wallHitPos[0] * GRAPHICS.scale, m_wallHitPos[1] * GRAPHICS.scale);
                float f = (float) (1 + (Math.sin(System.currentTimeMillis() / 100d) * 0.06125d));
                can.scale(f, f);

                can.drawCircle(0, 0, WALL_UNHIT_RANGE * GRAPHICS.scale, p);
            }
            can.restore();
        }

        can.save();
        {
            can.translate(this.m_pos.x * GRAPHICS.scale, this.m_pos.y * GRAPHICS.scale);
            can.rotate((float) m_direction);

            if (GameActivity.DEBUG)
            {
                Paint p = new Paint();
                p.setColor(Color.BLACK);
                p.setStyle(Paint.Style.FILL);
                can.drawCircle(0, 0, 3, p);
            }
            else
            {
                Bitmap b = IconProvider.getIconBitmap("player", 0);
                can.drawBitmap(b, new Rect(0, 0, b.getWidth(), b.getHeight()), new RectF(-16, -16, 16, 16), new Paint());
            }
        }
        can.restore();
    }
}
