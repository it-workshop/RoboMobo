import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Loredan on 09.12.13.
 */
public interface IDrawable
{
    /**
     * Draws this object.
     * @param time
     */
    public void draw(Canvas can, long time);
}
