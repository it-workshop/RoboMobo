import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class Map
{
    private static final float width = 100.0f;
    private static final float height = 100.0f;

    public class Obstacle implements Drawable
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
        public Bitmap getBitmap()
        {
            return m_bitmap;
        }

        @Override
        public float[] getCoords()
        {
            return new float[]{(boundaries.right - boundaries.left) / 2, (boundaries.top - boundaries.bottom) / 2};
        }
    }

    public ArrayList<Obstacle> obstacles;
}
