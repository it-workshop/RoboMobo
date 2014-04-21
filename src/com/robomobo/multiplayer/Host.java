package com.robomobo.multiplayer;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.robomobo.R;
import com.robomobo.model.Map;
import com.robomobo.model.Pickup;
import org.json.JSONException;

import java.util.Random;

/**
 * Created by Всеволод on 30.03.2014.
 */
public class Host
{
    private static Random mRandom;
    private static Map mMap;
    private static String mRoomId;
    private static GoogleApiClient mClient;
    public static int mLastPickupId = 0;
    public static boolean mInitialized = false;
    private static int mNotReady;
    private static Networking mNetworking;
    private static boolean mSpawning = false;

    public static void init(Networking networking)
    {
        Log.d("Multiplayer", "Host initializing");
        mInitialized = true;
        mNetworking = networking;
        mMap = networking.mActivity.m_currentMap;
        mRandom = new Random();
        mRoomId = networking.mRoomId;
        mClient = networking.mClient;
        networking.mCreationTimestamp = System.currentTimeMillis();
        try
        {
            mNetworking.reliableBroadcast(MultiplayerMessageCodec.encodeSync());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        mNotReady = networking.mRoomSize-1;
        mNetworking.mActivity.findViewById(R.id.host).setVisibility(View.VISIBLE);
        //spawnTimer();
    }

    public static void deinit()
    {
        mInitialized = false;
        mNetworking.mActivity.findViewById(R.id.host).setVisibility(View.INVISIBLE);
        stopSpawn();
    }

    public static void startSpawn()
    {
        Log.d("Multiplayer", "Start spawning");
        mSpawning = true;
        spawnTimer();
    }

    public static void stopSpawn()
    {
        Log.d("Multiplayer", "Stop spawning");
        mSpawning = false;
    }

    private static void spawnTimer()
    {
        if(!mSpawning)
            return;
        float x = mRandom.nextFloat()*mMap.getWidth();
        float y = mRandom.nextFloat()*mMap.getHeight();
        int type = mRandom.nextInt(2);
        long timestamp = System.currentTimeMillis() - mNetworking.mCreationTimestamp;
        int lifetime = mRandom.nextInt(15000)+5000;
        mNetworking.registerPickup(++mLastPickupId, timestamp, lifetime, x, y, type);
        mMap.registerObject(new Pickup(x, y, mLastPickupId, lifetime, Pickup.PickupType.getElementFromID(type)));
        try
        {
            mNetworking.reliableBroadcast(MultiplayerMessageCodec.encodeSpawn(mLastPickupId, System.currentTimeMillis()-mNetworking.mCreationTimestamp, lifetime, x, y, type));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
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

    public static void playerReady()
    {
        if(--mNotReady==0)
        {
            Log.d("Multiplayer", "All players ready");
            try
            {
                long startTimestamp = System.currentTimeMillis()-mNetworking.mCreationTimestamp+10000;
                mNetworking.reliableBroadcast(MultiplayerMessageCodec.encodeStart(startTimestamp));
                mNetworking.startTimer(startTimestamp);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
