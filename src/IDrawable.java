import android.graphics.Bitmap;

/**
 * Created by Loredan on 09.12.13.
 */
public interface IDrawable
{
    /**
     * Get the bitmap to draw from time.
     * @param time
     * May be used for animations.
     * @return
     */
    Bitmap getBitmap(long time);

    /**
     * Gets the coordinates of this drawable from time.
     * @param time
     * May be used for animations.
     * @return
     */
    float[] getCoords(long time);
}
