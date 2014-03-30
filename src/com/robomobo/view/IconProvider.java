package com.robomobo.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import com.robomobo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roman on 30.03.14.
 */
public class IconProvider
{
    private static Map<String, Icon> icons = new HashMap<String, Icon>();

    public static void init(Activity activity)
    {
        registerIcon(new Icon(new int[] {R.drawable.pickup_0_0, R.drawable.pickup_0_1, R.drawable.pickup_0_2,
                                        R.drawable.pickup_0_3, R.drawable.pickup_0_4, R.drawable.pickup_0_5,
                                        R.drawable.pickup_0_6, R.drawable.pickup_0_7, R.drawable.pickup_0_8,
                                        R.drawable.pickup_0_9, R.drawable.pickup_0_10, R.drawable.pickup_0_11,
                                        R.drawable.pickup_0_12, R.drawable.pickup_0_13, R.drawable.pickup_0_14,
                                        R.drawable.pickup_0_15, R.drawable.pickup_0_16, R.drawable.pickup_0_17,
                                        R.drawable.pickup_0_18, R.drawable.pickup_0_19, R.drawable.pickup_0_20,
                                        R.drawable.pickup_0_21, R.drawable.pickup_0_22, R.drawable.pickup_0_23,
                                        R.drawable.pickup_0_24}, true,  2, activity), "pickup_0");

        registerIcon(new Icon(new int[] {R.drawable.pickup_1_0, R.drawable.pickup_1_1, R.drawable.pickup_1_2,
                                        R.drawable.pickup_1_3, R.drawable.pickup_1_4, R.drawable.pickup_1_5,
                                        R.drawable.pickup_1_6, R.drawable.pickup_1_7, R.drawable.pickup_1_8,
                                        R.drawable.pickup_1_9, R.drawable.pickup_1_10, }, true,  3, activity), "pickup_1");

        registerIcon(new Icon(new int[] {R.drawable.wall_temp}, false, 0, activity), "wall");
        registerIcon(new Icon(new int[] {R.drawable.player_temp}, false, 0, activity), "player");
    }

    public static void registerIcon(Icon ic, String id)
    {
        icons.put(id, ic);
    }

    /**
     * Gets a bitmap of a certain frame of a certain icon.
     * @param id
     * ID of the icon requested.
     * @param extTimer
     * External timer used if the icon requested is async. Should be in milliseconds.
     * @return
     */
    public static Bitmap getIconBitmap(String id, int extTimer)
    {
        Icon ic = icons.get(id);
        if(ic != null)
        {
            if(ic.m_framerate == 0)
            {
                return ic.m_bitmaps.get(0);
            }

            if(ic.m_async)
            {
                return ic.m_bitmaps.get((extTimer / (60 / ic.m_framerate)) % ic.m_bitmaps.size());
            }
            else
            {
                return ic.getSyncedIcon();
            }
        }
        return null;
    }

    public static class Icon
    {
        /**
         * If this is true, then this icon shouldn't have a global timer and can only be animated from a value of an external timer.
         */
        public boolean m_async = false;
        /**
         * Amount of frames that should be shown for this icon per second.
         * 0 if this icon doesn't have an animation.
         */
        public int m_framerate = 0;
        /**
         * The frames of this icon.
         */
        public List<Bitmap> m_bitmaps = new ArrayList<Bitmap>();

        private int m_syncedTimer = -1;

        public Icon(int[] res, boolean async, int framerate, Activity activity)
        {
            for(int i = 0; i < res.length; i++)
            {
                this.m_bitmaps.add(BitmapFactory.decodeResource(activity.getResources(), res[i]));
            }
            this.m_async = async;
            this.m_framerate = framerate;

            if(res.length == 1)
            {
                this.m_framerate = 0;
            }

            if(!this.m_async && this.m_framerate != 0)
            {
                createSyncTimer();
            }
        }

        private void createSyncTimer()
        {
            m_syncedTimer = 0;
            new CountDownTimer(this.m_bitmaps.size() * m_framerate, m_framerate)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    m_syncedTimer++;
                }

                @Override
                public void onFinish()
                {
                    createSyncTimer();
                }
            }.start();
        }

        public Bitmap getSyncedIcon()
        {
            if(!this.m_async)
            {
                return this.m_bitmaps.get(m_syncedTimer / m_framerate);
            }
            return null;
        }
    }
}
