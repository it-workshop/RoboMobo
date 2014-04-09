package com.robomobo.multiplayer;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.robomobo.R;
import com.robomobo.model.GameActivity;
import com.robomobo.model.LocalPlayer;
import com.robomobo.model.Pickup;
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
    ArrayList<String> mParticipantIds;
    public String mRoomId;
    GoogleApiClient mClient;
    long mSeed = 0;
    int mSeedCounter;
    GameActivity mActivity;
    boolean mIsHost = false;
    long mCreationTimestamp;
    int mRoomSize;

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
        mRoomId = room.getRoomId();
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
            Log.d("Multiplayer", "All players connected");
            for(String participantId : room.getParticipantIds())
            {
                try
                {
                    Games.RealTimeMultiplayer.sendUnreliableMessage(mClient, MultiplayerMessageCodec.encodeSelf(), mRoomId, participantId);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e)
                {
                    mSelfId = participantId;
                    break;
                }
            }
            Random r = new Random();
            mActivity.m_players.put(mSelfId, new LocalPlayer(r.nextFloat()*100, r.nextFloat()*100)); //TODO: spawn points
            mParticipantIds = room.getParticipantIds();
            mRoomSize = mParticipantIds.size();
            int seed = (new Random()).nextInt();
            mSeed += seed;
            mSeedCounter += room.getParticipantIds().size()-1;
            chooseHost();
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
        mSeedCounter--;
        chooseHost();
    }

    private void chooseHost()
    {
        if(mSeedCounter==0)
        {
            Log.d("Multiplayer", "Choosing host");
            if (mParticipantIds.get(Math.abs((int) (mSeed % mRoomSize))) == mSelfId)
                Host.init(this);
        }
    }

    public void setTimestamp(String senderId)
    {
        mCreationTimestamp = System.currentTimeMillis();
        try
        {
            if(Host.mInitialized)
                Games.RealTimeMultiplayer.sendUnreliableMessage(mClient, MultiplayerMessageCodec.encodePing((int) (System.currentTimeMillis()-mCreationTimestamp)/2), mRoomId, senderId);
            else
                Games.RealTimeMultiplayer.sendUnreliableMessage(mClient, MultiplayerMessageCodec.encodeSync(), mRoomId, senderId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void pingCorrection(int ping, String senderId)
    {
        mCreationTimestamp-=ping;
        try
        {
            Games.RealTimeMultiplayer.sendUnreliableMessage(mClient, MultiplayerMessageCodec.encodeReady(), mRoomId, senderId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void startTimer(long startTimestamp)
    {
        Log.d("Multiplayer", "Start countdown");
        mActivity.findViewById(R.id.countdown).setVisibility(View.VISIBLE);
        new CountDownTimer(startTimestamp - System.currentTimeMillis() + mCreationTimestamp, 100)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                ((TextView) mActivity.findViewById(R.id.countdown)).setText(String.valueOf(millisUntilFinished / 1000 + 1));
            }

            @Override
            public void onFinish()
            {
                mActivity.findViewById(R.id.countdown).setVisibility(View.INVISIBLE);
                if(Host.mInitialized)
                    Host.startSpawn();
            }
        }.start();
    }

    public void registerPickup(int id, long timestamp, int lifetime, float x, float y, int type)
    {
        mActivity.m_currentMap.registerObject(new Pickup(x, y, id, (int) (lifetime-(timestamp-(System.currentTimeMillis()-mCreationTimestamp))), Pickup.PickupType.getElementFromID(type)));
    }
}
