package com.robomobo.multiplayer;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.robomobo.R;
import com.robomobo.model.GameActivity;
import com.robomobo.model.LocalPlayer;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Всеволод on 30.03.2014.
 */
public class Networking implements RoomUpdateListener, RoomStatusUpdateListener, RealTimeMessageReceivedListener
{
    public String mSelfId;
    String mRoomId;
    GoogleApiClient mClient;
    long mSeed = 0;
    int mSeedCounter;
    GameActivity mActivity;
    boolean mIsHost = false;
    long mCreationTimestamp;

    public Networking(GoogleApiClient client, GameActivity activity)
    {
        mClient = client;
        mActivity = activity;
        mActivity.m_players = new HashMap<String, com.robomobo.model.Player>();

    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage)
    {
        MultiplayerMessageCodec.decode(realTimeMessage, this);
    }

    @Override
    public void onRoomConnecting(Room room)
    {

    }

    @Override
    public void onRoomAutoMatching(Room room)
    {

    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> strings)
    {

    }

    @Override
    public void onPeerDeclined(Room room, List<String> strings)
    {

    }

    @Override
    public void onPeerJoined(Room room, List<String> strings)
    {

    }

    @Override
    public void onPeerLeft(Room room, List<String> strings)
    {

    }

    @Override
    public void onConnectedToRoom(Room room)
    {
        mSelfId = Games.Players.getCurrentPlayerId(mClient);
        mRoomId = room.getRoomId();
        Random r = new Random();
        mActivity.m_players.put(mSelfId, new LocalPlayer(r.nextFloat()*100, r.nextFloat()*100)); //TODO: spawn points
    }

    @Override
    public void onDisconnectedFromRoom(Room room)
    {

    }

    @Override
    public void onPeersConnected(Room room, List<String> strings)
    {

    }

    @Override
    public void onPeersDisconnected(Room room, List<String> strings)
    {

    }

    @Override
    public void onP2PConnected(String s)
    {
        Random r = new Random();
        mActivity.m_players.put(s, new com.robomobo.model.Player());
    }

    @Override
    public void onP2PDisconnected(String s)
    {
        mActivity.m_players.remove(s);
    }

    @Override
    public void onRoomCreated(int i, Room room)
    {

    }

    @Override
    public void onJoinedRoom(int i, Room room)
    {

    }

    @Override
    public void onLeftRoom(int i, String s)
    {

    }

    @Override
    public void onRoomConnected(int i, Room room)
    {
        if(i == GamesStatusCodes.STATUS_OK)
        {
            mCreationTimestamp = System.currentTimeMillis();
            Log.wtf("timestamp", Long.toString(System.currentTimeMillis()-room.getCreationTimestamp()));
            Log.wtf("ids", room.getParticipantIds().toString());
            int seed = (new Random()).nextInt();
            mSeed += seed;
            mSeedCounter += room.getParticipantIds().size()-1;
            try
            {
                Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mClient, MultiplayerMessageCodec.encodeSeed(seed), mRoomId);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void addSeed(int seed)
    {
        mSeed += seed;
        if(--mSeedCounter==0)
            Host.init(this);
    }

    public void setTimestamp(String senderId)
    {
        mCreationTimestamp = System.currentTimeMillis();
        Log.d("host initialized", String.valueOf(Host.mInitialized));
        new CountDownTimer(120000, 100)
        {

            @Override
            public void onTick(long millisUntilFinished)
            {
                ((TextView) mActivity.findViewById(R.id.timestamp)).setText(Long.toString(System.currentTimeMillis()-mCreationTimestamp));
            }

            @Override
            public void onFinish()
            {

            }
        }.start();
        try
        {
            if(Host.mInitialized)
                Games.RealTimeMultiplayer.sendUnreliableMessage(mClient, MultiplayerMessageCodec.encodePing((int) (System.currentTimeMillis()-mCreationTimestamp)/2), mRoomId, senderId);
            else
                Games.RealTimeMultiplayer.sendUnreliableMessage(mClient, MultiplayerMessageCodec.encodeTestSync(System.currentTimeMillis()), mRoomId, senderId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void pingCorrection(int ping)
    {
        mCreationTimestamp-=ping;
    }
}
