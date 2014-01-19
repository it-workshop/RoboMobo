package com.robomobo.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
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
    public void onCreate(Bundle savedInstanceState)
    {
        m_currentMap = new Map();
        m_currentMap.registerObject(new Map.Obstacle(10, 20, 30, 40, 0));
        m_currentMap.registerObject(new Map.Obstacle(50, 50, 70, 90, 0));
        m_players = new ArrayList<Player>();
        m_players.add(new LocalPlayer(60, 10));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ingame);
        GRAPHICS.init(this);
    }
}
