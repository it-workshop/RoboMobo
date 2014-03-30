package com.robomobo.multiplayer;

import android.os.CountDownTimer;
import com.robomobo.model.Map;
import com.robomobo.model.Pickup;

import java.util.Random;

/**
 * Created by Всеволод on 30.03.2014.
 */
public class Host
{
    private static Random mRandom;
    private static Map mMap;
    public static int lastPickupId = 0;

    public static void init(Map map)
    {
        mMap = map;
        mRandom = new Random();

        spawnTimer();
    }

    public static void spawnTimer()
    {
        mMap.registerObject(new Pickup(mRandom.nextInt(100), mRandom.nextInt(100), ++lastPickupId, mRandom.nextInt(2) == 0 ? Pickup.PickupType.RoundYellowThingyThatLooksLikeSun : Pickup.PickupType.BlueIcyCrystalStuff));
        int spawn = mRandom.nextInt(15000) + 5000;
        new CountDownTimer(spawn, spawn + 1)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {

            }

            @Override
            public void onFinish()
            {
                spawnTimer();
            }
        }.start();
    }
}
