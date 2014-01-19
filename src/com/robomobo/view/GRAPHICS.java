package com.robomobo.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.robomobo.R;

/**
 * Created by Roman on 16.01.14.
 */
public final class Graphics
{
    public final Bitmap PLAYER;
    public final Bitmap WALL;

    public Graphics(Activity activity)
    {
        PLAYER = BitmapFactory.decodeResource(activity.getResources(), R.drawable.player_temp);
        WALL = BitmapFactory.decodeResource(activity.getResources(), R.drawable.wall_temp);
    }
}
