import android.graphics.Bitmap;

/**
 * Created by Loredan on 09.12.13.
 */
public interface Drawable
{
    Bitmap m_bitmap = null;

    Bitmap getBitmap();

    float[] getCoords();
}
