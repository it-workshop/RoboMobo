import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Loredan on 08.12.13.
 */
public class Player implements IDrawable
{
    //NOTE: assuming OX goes to the right and OY goes up.

    protected float[] m_pos;
    protected int m_score;
    protected int m_id;

    public Player()
    {
        m_pos = new float[2];
    }

    public float getX()
    {
        return m_pos[0];
    }

    public float getY()
    {
        return m_pos[1];
    }

    public void changePos(float[] coord)
    {
        m_pos[0] = coord[0];
        m_pos[1] = coord[1];
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

        }
        can.restore();
    }
}
