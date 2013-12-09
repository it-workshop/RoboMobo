import android.graphics.Bitmap;

/**
 * Created by Loredan on 09.12.13.
 */
public interface Drawable
{
    Bitmap picture = null;

    Bitmap draw();

    float[] getCoords();
}
