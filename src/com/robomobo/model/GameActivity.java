package com.robomobo.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.robomobo.R;
import com.robomobo.view.GRAPHICS;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity
{
    public Map m_currentMap;
    public ArrayList<Player> m_players;
    private int currentPlayer = 0;

    public static boolean DEBUG = false;

    public void onCreate(Bundle savedInstanceState)
    {
        m_currentMap = new Map();
        m_currentMap.registerObject(new Map.Obstacle(10, 20, 30, 40, 0));
        m_currentMap.registerObject(new Map.Obstacle(50, 50, 70, 90, 0));
        m_players = new ArrayList<Player>();
        m_players.add(new LocalPlayer(60, 10));
        m_players.add(new Player(70, 30));
        m_players.add(new Player(38, 73));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ingame);
        GRAPHICS.init(this);

        ((ToggleButton) findViewById(R.id.toggleDebug)).setChecked(DEBUG);
    }

    public void nextPlayer(View view)
    {
        if(currentPlayer<m_players.size()-1)
            currentPlayer++;
        ((TextView) findViewById(R.id.currentPlayer)).setText(String.valueOf(currentPlayer));
    }

    public void prevPlayer(View view)
    {
        if(currentPlayer>0)
            currentPlayer--;
        ((TextView) findViewById(R.id.currentPlayer)).setText(String.valueOf(currentPlayer));
    }

    public void moveUp(View view)
    {
        PointF current = m_players.get(currentPlayer).getPos();
        current.offset(0, -1);
        m_players.get(currentPlayer).move(current, m_currentMap);
    }

    public void moveDown(View view)
    {
        PointF current = m_players.get(currentPlayer).getPos();
        current.offset(0, 1);
        m_players.get(currentPlayer).move(current, m_currentMap);
    }

    public void moveRight(View view)
    {
        PointF current = m_players.get(currentPlayer).getPos();
        current.offset(1, 0);
        m_players.get(currentPlayer).move(current, m_currentMap);
    }

    public void moveLeft(View view)
    {
        PointF current = m_players.get(currentPlayer).getPos();
        current.offset(-1, 0);
        m_players.get(currentPlayer).move(current, m_currentMap);
    }

    public void setDEBUG(View view)
    {
        if(((ToggleButton) view).isChecked())
            DEBUG = true;
        else
            DEBUG = false;
    }
}
