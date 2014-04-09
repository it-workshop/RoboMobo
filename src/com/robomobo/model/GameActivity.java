package com.robomobo.model;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.robomobo.R;
import com.robomobo.multiplayer.Host;
import com.robomobo.multiplayer.Networking;
import com.robomobo.view.IconProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends com.robomobo.multiplayer.BaseGameActivity
{
    public Map m_currentMap;
    public HashMap<String, Player> m_players;
    public String currentPlayer;
    private Networking listeners;
    public static boolean DEBUG = false;
    private boolean mIsMultiplayer = true;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //GRAPHICS.init(this);
        IconProvider.init(this);

        m_currentMap = new Map();
        m_currentMap.registerObject(new Map.Obstacle(10, 20, 30, 40, 0));
        m_currentMap.registerObject(new Map.Obstacle(50, 50, 70, 90, 0));
        listeners = new Networking(getApiClient(), this);
        setContentView(R.layout.layout_ingame);

        ((ToggleButton) findViewById(R.id.toggleDebug)).setChecked(DEBUG);
    }

    public void setDEBUG(View view)
    {
        DEBUG = ((ToggleButton) view).isChecked();
    }

    public void spawnPickup(View view)
    {
        Random r = new Random();
        //m_currentMap.registerObject(new Pickup(r.nextInt(100), r.nextInt(100), r.nextInt(2) == 0 ? Pickup.PickupType.RoundYellowThingyThatLooksLikeSun : Pickup.PickupType.BlueIcyCrystalStuff));
    }

    public void movePlayerL(View view)
    {
        m_players.get(listeners.mSelfId).moveRelative(-1, 0, m_currentMap);
    }

    public void movePlayerR(View view)
    {
        m_players.get(listeners.mSelfId).moveRelative(1, 0, m_currentMap);
    }

    public void movePlayerU(View view)
    {
        m_players.get(listeners.mSelfId).moveRelative(0, -1, m_currentMap);
	}

    public void movePlayerD(View view)
    {
        m_players.get(listeners.mSelfId).moveRelative(0, 1, m_currentMap);
    }

    @Override
    public void onSignInFailed()
    {

    }

    @Override
    public void onSignInSucceeded()
    {
        ((ToggleButton) findViewById(R.id.toggleSignIn)).setChecked(true);

        if(mIsMultiplayer)
        {
            Bundle criteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

            RoomConfig.Builder builder = RoomConfig.builder(listeners)
                    .setMessageReceivedListener(listeners)
                    .setRoomStatusUpdateListener(listeners);
            builder.setAutoMatchCriteria(criteria);
            RoomConfig config = builder.build();

            Games.RealTimeMultiplayer.create(getApiClient(), config);
        }
    }

    public void toggleSignIn(View view)
    {
        if(((ToggleButton) view).isChecked())
        {
            beginUserInitiatedSignIn();
        }
        else
        {
            if(Host.mInitialized)
                Host.stopSpawn();
            m_players.clear();
            m_currentMap.m_pickups.clear();
            Games.RealTimeMultiplayer.leave(getApiClient(), listeners, listeners.mRoomId);
            signOut();
        }
    }
}
