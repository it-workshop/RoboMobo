package com.robomobo.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.robomobo.R;

/**
 * Created by Roman on 16.01.14.
 */
public class GRAPHICS
{
    public static Bitmap PLAYER;
    public static Bitmap WALL;
    public static float scale = 1;

    public static void init(Activity activity)
    {
        PLAYER = BitmapFactory.decodeResource(activity.getResources(), R.drawable.player_temp);
        WALL = BitmapFactory.decodeResource(activity.getResources(), R.drawable.wall_temp);
    }
}
