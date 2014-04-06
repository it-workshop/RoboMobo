package com.robomobo.multiplayer;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.Room;
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
    public static int lastPickupId = 0;
    public static boolean mInitialized = false;

    public static void init(Networking networking)
    {
        mInitialized = true;
        Toast.makeText(networking.mActivity, "Host", Toast.LENGTH_LONG).show();
        mMap = networking.mActivity.m_currentMap;
        mRandom = new Random();
        mRoomId = networking.mRoomId;
        mClient = networking.mClient;
        Log.d("host", "host");
        try
        {
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mClient, MultiplayerMessageCodec.encodeTestSync(networking.mCreationTimestamp = System.currentTimeMillis()), mRoomId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        //spawnTimer();
    }

    public static void spawnTimer()
    {
        mMap.registerObject(new Pickup(mRandom.nextInt(100), mRandom.nextInt(100), ++lastPickupId, mRandom.nextInt(2) == 0 ? Pickup.PickupType.RoundYellowThingyThatLooksLikeSun : Pickup.PickupType.BlueIcyCrystalStuff));
        //Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mClient, MultiplayerMessageCodec.encodePickUp(lastPickupId, ))
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
