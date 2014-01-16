package com.robomobo.model;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
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
public class Map
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

    public Map()
    {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public boolean registerObject(Object obj)
    {
        if(obj != null)
        {
            if(obj instanceof IDrawable && !this.m_drawables.contains(obj))
            {
                this.m_drawables.add((IDrawable)obj);
                return true;
            }
            if(obj instanceof Obstacle && !this.m_obstacles.contains(obj))
            {
                this.m_obstacles.add((Obstacle)obj);
                return true;
            }
        }
        return false;
    }

    public static class Obstacle implements IDrawable
    {
        private RectF boundaries;
        private int type;

        public boolean check(float x, float y)
        {
            return boundaries.contains(x, y);
        }

        public float[] boundariesCrossing(float x1, float y1, float x2, float y2)
        {
            if (segmentsCrossing(x1, y1, x2, y2, boundaries.left, boundaries.top, boundaries.right, boundaries.top))
            {
                return new float[]{x1 + (x2 - x1) * (boundaries.top - y1) / (y2 - y1), boundaries.top};
            }
            if (segmentsCrossing(x1, y1, x2, y2, boundaries.left, boundaries.bottom, boundaries.right, boundaries.bottom))
            {
                return new float[]{x1 + (x2 - x1) * (boundaries.bottom - y1) / (y2 - y1), boundaries.bottom};
            }
            if (segmentsCrossing(x1, y1, x2, y2, boundaries.left, boundaries.top, boundaries.left, boundaries.bottom))
            {
                return new float[]{boundaries.left, y1 + (y2 - y1) * (boundaries.left - x1) / (x2 - x1)};
            }
            if (segmentsCrossing(x1, y1, x2, y2, boundaries.left, boundaries.top, boundaries.left, boundaries.bottom))
            {
                return new float[]{boundaries.left, y1 + (y2 - y1) * (boundaries.left - x1) / (x2 - x1)};
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
            {

            }
            can.restore();
        }
    }
}
