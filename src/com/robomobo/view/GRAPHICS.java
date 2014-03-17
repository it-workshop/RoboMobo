package com.robomobo.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.robomobo.R;

import java.util.ArrayList;

/**
 * Created by Roman on 16.01.14.
 */
public class GRAPHICS
{
    public static Bitmap PLAYER;
    public static Bitmap WALL;
    public static ArrayList<Bitmap> PICKUP_0;
    public static float scale = 1;

    public static void init(Activity activity)
    {
        PLAYER = BitmapFactory.decodeResource(activity.getResources(), R.drawable.player_temp);
        WALL = BitmapFactory.decodeResource(activity.getResources(), R.drawable.wall_temp);
        PICKUP_0 = new ArrayList<Bitmap>(25);
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_0));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_1));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_2));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_3));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_4));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_5));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_6));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_7));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_8));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_9));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_10));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_11));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_12));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_13));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_14));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_15));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_16));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_17));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_18));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_19));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_20));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_21));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_22));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_23));
        PICKUP_0.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pickup_0_24));
    }
}
