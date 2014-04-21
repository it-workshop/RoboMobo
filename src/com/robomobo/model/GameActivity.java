package com.robomobo.model;

import com.robomobo.view.IDrawable;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.robomobo.R;
import com.robomobo.multiplayer.Host;
import com.robomobo.multiplayer.Networking;
import com.robomobo.view.IconProvider;
import com.robomobo.view.IDrawable;
import com.robomobo.view.IconProvider;
import com.robomobo.view.SurfaceViewIngame;
import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends com.robomobo.multiplayer.BaseGameActivity implements IDrawable
{
    public Map m_currentMap;
    public HashMap<String, Player> m_players;
    public Networking mNetworking;
    public static boolean DEBUG = false;
    private boolean mIsMultiplayer = true;


    public float m_lastPressedX = -1;
    public float m_lastPressedY = -1;
    public float m_pressedX = -1;
    public float m_pressedY = -1;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //GRAPHICS.init(this);
        IconProvider.init(this);

        m_currentMap = new Map(this);
        m_currentMap.registerObject(new Map.Obstacle(10, 20, 30, 40, 0));
        m_currentMap.registerObject(new Map.Obstacle(50, 50, 70, 90, 0));
        mNetworking = new Networking(getApiClient(), this);
        setContentView(R.layout.layout_ingame);

        ((ToggleButton) findViewById(R.id.toggleDebug)).setChecked(DEBUG);


        ((SurfaceViewIngame) findViewById(R.id.view)).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    m_lastPressedX = motionEvent.getX();
                    m_lastPressedY = motionEvent.getY();
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                {
                    m_pressedX = motionEvent.getX();
                    m_pressedY = motionEvent.getY();
                    new CountDownTimer(1000, 100)
                    {
                        @Override
                        public void onTick(long millisUntilFinished)
                        {
                            if(m_lastPressedX == -1 || m_lastPressedY == -1) return;

                            Player p = m_players.get(mNetworking.mSelfId);


                            double d = Math.atan((m_pressedX - m_lastPressedX)/(m_pressedY - m_lastPressedY));
                            if((m_pressedY - m_lastPressedY) < 0) d += Math.PI;
                            float direction = (float) (Math.toDegrees(d * -1) + 90f);

                            float strength = (float) Math.sqrt(Math.pow(m_pressedX - m_lastPressedX, 2) + Math.pow(m_pressedY - m_lastPressedY, 2));

                            p.moveRelative((float)(strength / 1000f * Math.cos(Math.toRadians(direction))), (float)(strength / 1000f * Math.sin(Math.toRadians(direction))));
                        }

                        @Override
                        public void onFinish()
                        {

                        }
                    }.start();
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    m_lastPressedX = -1;
                    m_lastPressedY = -1;
                }
                return true;
            }
        });
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
        ((LocalPlayer) m_players.get(mNetworking.mSelfId)).moveRelative(-1, 0, m_currentMap, mNetworking);
    }

    public void movePlayerR(View view)
    {
        ((LocalPlayer) m_players.get(mNetworking.mSelfId)).moveRelative(1, 0, m_currentMap, mNetworking);
    }

    public void movePlayerU(View view)
    {
        ((LocalPlayer) m_players.get(mNetworking.mSelfId)).moveRelative(0, -1, m_currentMap, mNetworking);
	}

    public void movePlayerD(View view)
    {
        ((LocalPlayer) m_players.get(mNetworking.mSelfId)).moveRelative(0, 1, m_currentMap, mNetworking);
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

            RoomConfig.Builder builder = RoomConfig.builder(mNetworking)
                    .setMessageReceivedListener(mNetworking)
                    .setRoomStatusUpdateListener(mNetworking);
            builder.setAutoMatchCriteria(criteria);
            RoomConfig config = builder.build();

            Games.RealTimeMultiplayer.create(getApiClient(), config);
        }
    }

    public void toggleSignIn(View view)
    {
        if (((ToggleButton) view).isChecked())
        {
            beginUserInitiatedSignIn();
        }
        else
        {
            if (Host.mInitialized)
                Host.stopSpawn();
            m_players.clear();
            m_currentMap.m_pickups.clear();
            Games.RealTimeMultiplayer.leave(getApiClient(), mNetworking, mNetworking.mRoomId);
            signOut();
        }
    }

    @Override
    public void draw(Canvas can, long time)
    {
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        can.drawCircle(m_lastPressedX, m_lastPressedY, 2f, p);
    }
}
