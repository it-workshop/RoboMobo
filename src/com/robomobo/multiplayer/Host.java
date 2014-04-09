package com.robomobo.multiplayer;

import android.os.CountDownTimer;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
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
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mClient, MultiplayerMessageCodec.encodeSync(), mRoomId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        mNotReady = networking.mRoomSize-1;
        //spawnTimer();
    }

    public static void deinit()
    {
        mInitialized = false;
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
        mMap.registerObject(new Pickup(x, y, ++mLastPickupId, mRandom.nextInt(2) == 0 ? Pickup.PickupType.RoundYellowThingyThatLooksLikeSun : Pickup.PickupType.BlueIcyCrystalStuff));
        /*try
        {
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mClient, MultiplayerMessageCodec.encodeSpawn(mLastPickupId, System.currentTimeMillis()-mNetworking.mCreationTimestamp, x, y, type), mRoomId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }*/
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
                Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mClient, MultiplayerMessageCodec.encodeStart(startTimestamp), mRoomId);
                mNetworking.startTimer(startTimestamp);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}