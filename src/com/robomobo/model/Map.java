package com.robomobo.model;

import android.graphics.*;
import android.os.CountDownTimer;
import android.util.Log;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;
import com.robomobo.view.IconProvider;

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
    public List<Obstacle> m_obstacles = new ArrayList<Obstacle>();
    public List<Pickup> m_pickups = new ArrayList<Pickup>();
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

    public void registerObject(Object obj)
    {
        if (obj != null)
        {
            if (obj instanceof IDrawable && !this.m_drawables.contains(obj))
            {
                this.m_drawables.add((IDrawable) obj);
            }
            if (obj instanceof Obstacle && !this.m_obstacles.contains(obj))
            {
                this.m_obstacles.add((Obstacle) obj);
            }
            if (obj instanceof Pickup && !this.m_pickups.contains(obj))
            {
                this.m_pickups.add((Pickup) obj);
                ((Pickup) obj).register(this);
            }
        }
    }

    @Override
    public void draw(Canvas can, long time)
    {
        Paint field_paint = new Paint();

        field_paint.setColor(Color.WHITE);
        can.drawRect(0, 0, GRAPHICS.scale * m_width, GRAPHICS.scale * m_height, field_paint);
        field_paint.setStyle(Paint.Style.STROKE);
        field_paint.setColor(Color.MAGENTA);
        can.drawRect(0, 0, GRAPHICS.scale * m_width, GRAPHICS.scale * m_height, field_paint);
        field_paint.reset();


        //for (IDrawable drawable : m_drawables)
        for(int i = 0; i < m_drawables.size(); i++)
        {
            IDrawable drawable = m_drawables.get(i);
            drawable.draw(can, time);
        }
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
            float[] result = new float[2];
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.left, m_boundaries.top,
                    m_boundaries.right, m_boundaries.top))
            {
                result[0]=x1 + (x2 - x1) * (m_boundaries.top - y1) / (y2 - y1);
                result[1]=m_boundaries.top;
                if(result[0]==result[0] && result[1]==result[1]) //NaN check
                    return result;
            }
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.left, m_boundaries.bottom,
                    m_boundaries.right, m_boundaries.bottom))
            {
                result[0]=x1 + (x2 - x1) * (m_boundaries.bottom - y1) / (y2 - y1);
                result[1]=m_boundaries.bottom;
                if(result[0]==result[0] && result[1]==result[1])
                    return result;
            }
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.left, m_boundaries.top,
                    m_boundaries.left, m_boundaries.bottom))
            {
                result[0]=m_boundaries.left;
                result[1]=y1 + (y2 - y1) * (m_boundaries.left - x1) / (x2 - x1);
                if(result[0]==result[0] && result[1]==result[1])
                    return result;
            }
            if (segmentsCrossing(x1, y1, x2, y2, m_boundaries.right, m_boundaries.top,
                    m_boundaries.right, m_boundaries.bottom))
            {
                result[0]=m_boundaries.right;
                result[1]=y1 + (y2 - y1) * (m_boundaries.right - x1) / (x2 - x1);
                if(result[0]==result[0] && result[1]==result[1])
                    return result;
            }
            Log.e("Collision detection", "No crossing at all");
            return new float[] {(x1+x2)/2, (y1+y2)/2}; //if something still goes wrong...
        }

        private boolean segmentsCrossing(float a1x, float a1y, float a2x, float a2y, float b1x,
                float b1y, float b2x, float b2y)
        {
            if (pointOnSegment(a1x, a1y, b1x, b1y, b2x, b2y))
                return true;
            if (pointOnSegment(a2x, a2y, b1x, b1y, b2x, b2y))
                return true;
            if (pointOnSegment(b1x, b1y, a1x, a1y, a2x, a2y))
                return true;
            if (pointOnSegment(b2x, b2y, a1x, a1y, a2x, a2y))
                return true;
            //[A1A2, A1B1]*[A1A2, A1B2]<0 && [B1B2, B1A1]*[B1B2, B1A2]<0
            return (((a2x - a1x) * (b1y - a1y) - (a2y - a1y) * (b1x - a1x)) *
                    ((a2x - a1x) * (b2y - a1y) - (a2y - a1y) * (b2x - a1x)) < 0) &&
                    (((b2x - b1x) * (a1y - b1y) - (b2y - b1y) * (a1x - b1x)) *
                            ((b2x - b1x) * (a2y - b1y) - (b2y - b1y) * (a2x - b1x)) < 0);
        }

        private boolean pointOnSegment(float ax, float ay, float b1x, float b1y, float b2x,
                float b2y)
        {
            return ((b1x - ax) * (b2y - ay) - (b1y - ay) * (b2x - ax) == 0) &&
                    ((b1x - ax) * (b2x - ax) + (b1y - ay) * (b2y - ay) <= 0);
        }

        @Override
        public void draw(Canvas can, long time)
        {
            can.save();

            can.scale(GRAPHICS.scale, GRAPHICS.scale);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);

            if (GameActivity.DEBUG) can.drawRect(m_boundaries, paint);
            else
            {
                Bitmap b = IconProvider.getIconBitmap("wall", 0);
                can.drawBitmap(b, new Rect(0, 0, b.getWidth(), b.getHeight()), m_boundaries, paint);
            }

            can.restore();
        }
    }
}
