package com.robomobo.model;

import android.graphics.*;
import android.os.CountDownTimer;
import android.util.Log;
import com.google.android.gms.games.Games;
import com.robomobo.multiplayer.MultiplayerMessageCodec;
import com.robomobo.multiplayer.Networking;
import com.robomobo.view.GRAPHICS;
import com.robomobo.view.IDrawable;
import com.robomobo.view.IconProvider;
import org.json.JSONException;

/**
 * Created by Vsevolod on 17.03.14.
 */
public class Pickup implements IDrawable
{
    public PickupType m_type = PickupType.none;
    private int m_lifetime_ms; //lifetime in milliseconds
    private int mFullLifetime;
    private PointF m_coords;
    private Map m_mapReference;
    public int mId;
    private int mPlayersNotConfirmed;
    private boolean mPickedUp = false;
    private long mPickUpTimestamp;
    private String mPickedUpParticipantId;

    public Pickup(float x, float y, int id, int lifetime)
    {
        m_coords = new PointF(x, y);
        mId = id;
        mFullLifetime = lifetime;
    }

    public Pickup(float x, float y, int id, int lifetime, PickupType type)
    {
        this(x, y, id, lifetime);
        m_type = type;
    }

    @Override
    public void draw(Canvas can, long time)
    {
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        if(GameActivity.DEBUG)
            can.drawCircle(m_coords.x*GRAPHICS.scale, m_coords.y*GRAPHICS.scale, 3, p);
        else
        {
            Bitmap b = IconProvider.getIconBitmap(m_type.m_iconId, m_lifetime_ms);
            int f = 0;
            switch(m_type)
            {                                                              //Left here for special fancy rendering cases.
                /*case RoundYellowThingyThatLooksLikeSun:

                    f = (int)((m_lifetime_ms / GRAPHICS.PICKUP_0_FRAMERATE) % GRAPHICS.PICKUP_0.size());

                    can.drawBitmap(GRAPHICS.PICKUP_0.get(f), m_coords.x*GRAPHICS.scale-GRAPHICS.PICKUP_0.get(f).getWidth()/2, m_coords.y*GRAPHICS.scale-GRAPHICS.PICKUP_0.get(f).getHeight()/2, p);
                    break;

                case BlueIcyCrystalStuff:
                    f = (int)((m_lifetime_ms / GRAPHICS.PICKUP_1_FRAMERATE) % GRAPHICS.PICKUP_1.size());
                    can.drawBitmap(GRAPHICS.PICKUP_1.get(f), m_coords.x*GRAPHICS.scale-GRAPHICS.PICKUP_1.get(f).getWidth()/2, m_coords.y*GRAPHICS.scale-GRAPHICS.PICKUP_1.get(f).getHeight()/2, p);
                    break; */

                default:
                    if(b == null) can.drawCircle(m_coords.x*GRAPHICS.scale, m_coords.y*GRAPHICS.scale, 3, p);
                    else can.drawBitmap(b, m_coords.x*GRAPHICS.scale-b.getWidth()/2, m_coords.y*GRAPHICS.scale-b.getHeight()/2, p);
                    break;
            }

        }
    }

    public void register(Map map)
    {
        m_mapReference = map;
        mPlayersNotConfirmed = m_mapReference.mActivity.mNetworking.mRoomSize-1;

        new CountDownTimer(mFullLifetime, 10)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                m_lifetime_ms+=10;
            }

            @Override
            public void onFinish()
            {
                onRemoved();
            }
        }.start();
    }

    public PointF getPosition()
    {
        return this.m_coords;
    }

    public void onPickedUp()
    {
        if(mPickedUp)
            return;
        mPickedUp = true;
        mPickedUpParticipantId = m_mapReference.mActivity.mNetworking.mSelfId;
        mPickUpTimestamp = System.currentTimeMillis()-m_mapReference.mActivity.mNetworking.mCreationTimestamp;
        try
        {
            m_mapReference.mActivity.mNetworking.reliableBroadcast(MultiplayerMessageCodec.encodePickUp(mId, System.currentTimeMillis()-m_mapReference.mActivity.mNetworking.mCreationTimestamp));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        m_mapReference.m_drawables.remove(this);
        Log.d("Multiplayer", "Drawable removed");
    }

    public void onPickedUp(String participantId, long timestamp)
    {
        if(mPickedUp)
        {
            if(timestamp<mPickUpTimestamp)
            {
                mPickedUpParticipantId = participantId;
                mPickUpTimestamp = timestamp;
                try
                {
                    m_mapReference.mActivity.mNetworking.reliableMessage(MultiplayerMessageCodec.encodeConfirm(mId), participantId);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            mPickedUp = true;
            mPickUpTimestamp = timestamp;
            mPickedUpParticipantId = participantId;
            m_mapReference.m_drawables.remove(this);
            Log.d("Multiplayer", "Drawable removed " + mId);
            try
            {
                m_mapReference.mActivity.mNetworking.reliableMessage(MultiplayerMessageCodec.encodeConfirm(mId), participantId);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void onRemoved()
    {
        m_mapReference.m_drawables.remove(this);
        Log.d("Multiplayer", "Drawable removed " + mId);
        m_mapReference.m_pickups.remove(this);
        Log.d("Multiplayer", "Pickup removed " + mId);
    }

    public void onConfirmation()
    {
        if(--mPlayersNotConfirmed==0)
        {
            try
            {
                m_mapReference.mActivity.mNetworking.reliableBroadcast(MultiplayerMessageCodec.encodeScore(mId));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            m_mapReference.mActivity.m_players.get(m_mapReference.mActivity.mNetworking.mSelfId).addScore(m_type.m_points);
            m_mapReference.mActivity.updateScores();
            m_mapReference.m_pickups.remove(this);
            Log.d("Multiplayer", "Pickup removed " + mId);
        }
    }

    public void score(String senderParticipantId)
    {
        m_mapReference.mActivity.m_players.get(senderParticipantId).addScore(m_type.m_points);
        m_mapReference.mActivity.updateScores();
        m_mapReference.m_pickups.remove(this);
        Log.d("Multiplayer", "Pickup removed " + mId);
    }

    public static enum  PickupType
    {
        none(-1, 1, ""),
        RoundYellowThingyThatLooksLikeSun(0, 10, "pickup_0"),                   //Yes, such long type names are necessary. NECESSARY I SAY!
        BlueIcyCrystalStuff(1, 20, "pickup_1");

        public int m_points;
        public String m_iconId;
        public int m_Id;

        private PickupType(int id, int p, String s)
        {
            m_Id = id;
            m_points = p;
            m_iconId = s;
        }

        public static PickupType getElementFromID(int id)
        {
            switch(id)
            {
                case -1: return none;
                case 0: return RoundYellowThingyThatLooksLikeSun;
                case 1: return BlueIcyCrystalStuff;
            }

            return null;
        }
    }
}
