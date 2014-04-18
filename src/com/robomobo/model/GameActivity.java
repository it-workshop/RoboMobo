package com.robomobo.model;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.robomobo.R;
import com.robomobo.view.IDrawable;
import com.robomobo.view.IconProvider;
import com.robomobo.view.SurfaceViewIngame;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity implements IDrawable
{
    public Map m_currentMap;
    public ArrayList<Player> m_players;
    private int currentPlayer = 0;

    public static boolean DEBUG = false;


    public float m_lastPressedX = -1;
    public float m_lastPressedY = -1;
    public float m_pressedX = -1;
    public float m_pressedY = -1;

    public void onCreate(Bundle savedInstanceState)
    {
        //GRAPHICS.init(this);
        IconProvider.init(this);
        m_currentMap = new Map();
        m_currentMap.registerObject(new Map.Obstacle(10, 20, 30, 40, 0));
        m_currentMap.registerObject(new Map.Obstacle(50, 50, 70, 90, 0));
        m_currentMap.registerObject(new Pickup(50, 40, Pickup.PickupType.RoundYellowThingyThatLooksLikeSun));
        m_currentMap.registerObject(new Pickup(10, 60, Pickup.PickupType.BlueIcyCrystalStuff));
        m_currentMap.registerObject(this);
        m_players = new ArrayList<Player>();
        m_players.add(new LocalPlayer(60, 10));
        m_players.add(new Player(70, 30));
        m_players.add(new Player(38, 73));
        super.onCreate(savedInstanceState);
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

                            Player p = m_players.get(currentPlayer);


                            double d = Math.atan((m_pressedX - m_lastPressedX)/(m_pressedY - m_lastPressedY));
                            if((m_pressedY - m_lastPressedY) < 0) d += Math.PI;
                            float direction = (float) (Math.toDegrees(d * -1) + 90f);

                            float strength = (float) Math.sqrt(Math.pow(m_pressedX - m_lastPressedX, 2) + Math.pow(m_pressedY - m_lastPressedY, 2));

                            p.moveRelative((float)(strength / 1000f * Math.cos(Math.toRadians(direction))), (float)(strength / 1000f * Math.sin(Math.toRadians(direction))), m_currentMap);
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
        m_currentMap.registerObject(new Pickup(r.nextInt(100), r.nextInt(100), r.nextInt(2) == 0 ? Pickup.PickupType.RoundYellowThingyThatLooksLikeSun : Pickup.PickupType.BlueIcyCrystalStuff));
    }

    public void movePlayerL(View view)
    {
        m_players.get(currentPlayer).moveRelative(-1, 0, m_currentMap);
    }

    public void movePlayerR(View view)
    {
        m_players.get(currentPlayer).moveRelative(1, 0, m_currentMap);
    }

    public void movePlayerU(View view)
    {
        m_players.get(currentPlayer).moveRelative(0, -1, m_currentMap);
	}

    public void movePlayerD(View view)
    {
        m_players.get(currentPlayer).moveRelative(0, 1, m_currentMap);
    }
    public void nextPlayer(View view)
    {
        if(currentPlayer<m_players.size()-1) currentPlayer++;
        ((TextView) findViewById(R.id.currentPlayer)).setText(String.valueOf(currentPlayer));
    }

    public void prevPlayer(View view)
    {
        if(currentPlayer>0)
            currentPlayer--;
        ((TextView) findViewById(R.id.currentPlayer)).setText(String.valueOf(currentPlayer));
    }

    @Override
    public void draw(Canvas can, long time)
    {
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        can.drawCircle(m_lastPressedX, m_lastPressedY, 2f, p);
    }
}
