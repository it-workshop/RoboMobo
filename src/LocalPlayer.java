import android.graphics.Bitmap;
import android.graphics.Matrix;

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
    public double m_wallHitX;
    public double m_wallHitY;

    // probably we will use this constant somewhere else. Consider having a separate class for that
    public static final float WALL_UNHIT_RANGE = 5.0f;

    public LocalPlayer(float initX, float initY)
    {
        m_id = IdGenerator.getInstance().generatePlayerId();
        m_pos[0] = initX;
        m_pos[1] = initY;
        m_direction = 0;
        m_score = 0;
        m_wallHit = false;
        m_wallHitX = 0;
        m_wallHitY = 0;
    }

    public void move(float x, float y, Map map)
    {
        float[] direction = new float[2];
        float distance = (float) Math.sqrt(Math.pow(x - m_pos[0], 2) + Math.pow(y - m_pos[1], 2));
        for (int i = 0; i < 2; i++)
        {
            direction[i] = (float) ((x - m_pos[i]) / distance);
        }
        m_direction = direction[1] >= 0 ? Math.acos(direction[0]) : 2 * Math.PI - Math.acos(direction[0]);

        if (!m_wallHit)
        {
            for (Map.Obstacle obstacle : map.obstacles)
            {
                if (obstacle.check(x, y))
                {
                    // Ax+By+C=0
                    float A, B, C;
                    A = (float) Math.cos(m_direction + Math.PI / 2);
                    B = (float) Math.sin(m_direction + Math.PI / 2);
                    C = -A * x - B * y;
                    float[] temp;
                    try
                    {
                        temp = obstacle.boundariesCrossing(A, B, C, m_pos[0], m_pos[1]);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        temp = new float[]{0.0f, 0.0f};
                    }
                    m_wallHit = true;
                    m_wallHitX = temp[0];
                    m_wallHitY = temp[1];
                }
            }
        }
        else
        {
            if (Math.sqrt((x - m_wallHitX) * (x - m_wallHitX) + (y - m_wallHitY) * (y - m_wallHitY)) < WALL_UNHIT_RANGE)
            {
                boolean stillInsideTheWall = false;
                for (Map.Obstacle obstacle : map.obstacles)
                {
                    if (obstacle.check(x, y))
                        stillInsideTheWall = true;
                }
                if (!stillInsideTheWall)
                    m_wallHit = false;
            }
        }
        m_pos[0] = x;
        m_pos[1] = y;
    }

    @Override
    public Bitmap getBitmap()
    {
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate((float) m_direction, m_bitmap.getWidth(), m_bitmap.getHeight());
        return Bitmap.createBitmap(m_bitmap, 0, 0, m_bitmap.getWidth(), m_bitmap.getHeight(), rotationMatrix, true);
    }
}