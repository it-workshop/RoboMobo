package com.robomobo.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class Map implements IDrawable
{
    public static final float DEFAULT_WIDTH = 100.0f;
    public static final float DEFAULT_HEIGHT = 100.0f;
    private final float m_width;
    private final float m_height;
    public List<Obstacle> m_obstacles;
    public List<IDrawable> m_drawables = new ArrayList<IDrawable>();

    public Map(float width, float height)
    {
        this.m_width = width;
        this.m_height = height;
    }

    public float getWidth()
    {
        return m_width;
    }

    public float getHeight()
    {
        return m_height;
    }

    public Map()
    {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public boolean registerObject(Object obj)
    {
        if (obj != null)
        {
            if (obj instanceof IDrawable && !this.m_drawables.contains(obj))
            {
                this.m_drawables.add((IDrawable) obj);
                return true;
            }
            if (obj instanceof Obstacle && !this.m_obstacles.contains(obj))
            {
                this.m_obstacles.add((Obstacle) obj);
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas can, long time)
    {
        Paint field_paint = new Paint();
        field_paint.setStyle(Paint.Style.STROKE);
        field_paint.setARGB(127, 255, 255, 255);
        can.drawRect(0, 0, GRAPHICS.scale * m_width, GRAPHICS.scale * m_height, field_paint);
        for (IDrawable drawable : m_drawables)
            drawable.draw(can, time);
    }

    public static class Obstacle implements IDrawable
    {
        private RectF m_boundaries;
        private int m_type;

        public Obstacle(float left, float top, float right, float bottom, int type)
        {
            m_boundaries = new RectF(left, top, right, bottom);
            m_type = type;
        }

        public boolean check(float x, float y)
        {
            return m_boundaries.contains(x, y);
        }

        public float[] boundariesCrossing(float x1, float y1, float x2, float y2)
        {
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.left, m_boundaries.top, m_boundaries.right, m_boundaries.top))
            {
                return new float[]{x1 + (x2 - x1) * (m_boundaries.top - y1) / (y2 - y1), m_boundaries.top};
            }
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.left, m_boundaries.bottom, m_boundaries.right, m_boundaries.bottom))
            {
                return new float[]{x1 + (x2 - x1) * (m_boundaries.bottom - y1) / (y2 - y1), m_boundaries.bottom};
            }
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.left, m_boundaries.top, m_boundaries.left, m_boundaries.bottom))
            {
                return new float[]{m_boundaries.left, y1 + (y2 - y1) * (m_boundaries.left - x1) / (x2 - x1)};
            }
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.left, m_boundaries.top, m_boundaries.left, m_boundaries.bottom))
            {
                return new float[]{m_boundaries.left, y1 + (y2 - y1) * (m_boundaries.left - x1) / (x2 - x1)};
            }
            Log.e("Collision detection", "No crossing at all");
            return new float[]{0, 0};
        }

        private boolean segmentsCrossing(float a1x, float a1y, float a2x, float a2y, float b1x, float b1y, float b2x, float b2y)
        {
            float a1a2x = a2x - a1x;
            float a1a2y = a2y - a1y;
            float a1b1x = b1x - a1x;
            float a1b1y = b1y - a1y;
            float a1b2x = b2x - a1x;
            float a1b2y = b2y - a1y;
            //[A1A2, A1B1]*[A1A2, A1B2]<0
            return (a1a2x * a1b1y - a1a2y * a1b1x) * (a1a2x * a1b2y - a1a2y * a1b2x) < 0;
        }

        @Override
        public void draw(Canvas can, long time)
        {
            can.save();

            can.scale(GRAPHICS.scale, GRAPHICS.scale);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(127, 255, 0, 0);
            can.drawRect(m_boundaries, paint);

            can.restore();
        }
    }
}
