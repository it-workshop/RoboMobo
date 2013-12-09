import android.graphics.Bitmap;
import android.graphics.RectF;

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

        public float[] boundariesCrossing(float A, float B, float C, float x, float y) throws Exception
        {
            //Ax+By+C=0
            float[] result = new float[2];
            float[] crossingDistances = new float[4];
            crossingDistances[0] = Math.abs(x + y - boundaries.right + (A * boundaries.right + C) / B);
            crossingDistances[1] = Math.abs(x + y + (B * boundaries.top + C) / A - boundaries.top);
            crossingDistances[2] = Math.abs(x + y - boundaries.left + (A * boundaries.left + C) / B);
            crossingDistances[3] = Math.abs(x + y + (B * boundaries.bottom + C) / A - boundaries.bottom);
            if (crossingDistances[0] <= crossingDistances[1] && crossingDistances[0] <= crossingDistances[2] && crossingDistances[0] <= crossingDistances[3])
            {
                result[0] = boundaries.right;
                result[1] = (A * boundaries.right + C) / B;
            }
            else if (crossingDistances[1] <= crossingDistances[0] && crossingDistances[1] <= crossingDistances[2] && crossingDistances[1] <= crossingDistances[3])
            {
                result[0] = boundaries.right;
                result[1] = (A * boundaries.right + C) / B;
            }
            else if (crossingDistances[2] <= crossingDistances[1] && crossingDistances[2] <= crossingDistances[0] && crossingDistances[2] <= crossingDistances[3])
            {
                result[0] = boundaries.right;
                result[1] = (A * boundaries.right + C) / B;
            }
            else if (crossingDistances[3] <= crossingDistances[1] && crossingDistances[3] <= crossingDistances[2] && crossingDistances[3] <= crossingDistances[0])
            {
                result[0] = boundaries.right;
                result[1] = (A * boundaries.right + C) / B;
            }
            else
                throw new Exception("WTF??? No minimal element: " + crossingDistances[0] + " " + crossingDistances[1] + " " + crossingDistances[2] + " " + crossingDistances[3]);
            return result;
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
