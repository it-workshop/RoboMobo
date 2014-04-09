package com.robomobo.multiplayer;

import android.util.Log;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Всеволод on 30.03.2014.
 */
public class MultiplayerMessageCodec
{
    public static byte[] encodeSeed(int seed) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "seed");
        json.put("seed", seed);
        return json.toString().getBytes();
    }

    public static byte[] encodeReady() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "ready");
        return json.toString().getBytes();
    }

    public static byte[] encodeMove(float x, float y) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "move");
        json.put("x", x);
        json.put("y", y);
        return json.toString().getBytes();
    }

    public static byte[] encodePickUp(int id, long timestamp) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "pickup");
        json.put("id", id);
        json.put("timestamp", timestamp);
        return json.toString().getBytes();
    }

    public static byte[] encodeSpawn(int id, long timestamp, float x, float y) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "spawn");
        json.put("id", id);
        json.put("timestamp", timestamp);
        return json.toString().getBytes();
    }

    public static byte[] encodeConfirm(int id) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "confirm_pickup");
        json.put("id", id);
        return json.toString().getBytes();
    }

    public static byte[] encodeSync() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "sync");
        return json.toString().getBytes();
    }

    public static byte[] encodePing(int ping) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "ping");
        json.put("ping", ping);
        return json.toString().getBytes();
    }

    public static byte[] encodeStart(long startTimestamp) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "start");
        json.put("timestamp", startTimestamp);
        return json.toString().getBytes();
    }
    public static byte[] encodeSelf() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "self");
        return json.toString().getBytes();
    }

    public static void decode(RealTimeMessage realTimeMessage, Networking networking)
    {
        JSONObject json;
        try
        {
            json = new JSONObject(new String(realTimeMessage.getMessageData()));
            String type = json.getString("type");
            Log.d("Multiplayer", "Incoming message, type: " + type);
            if(type.equals("seed"))
            {
                networking.addSeed(json.getInt("seed"));
            }
            else if(type.equals("ready"))
            {
                Host.playerReady();
            }
            else if(type.equals("move"))
            {
                //TODO: move remote players
            }
            else if(type.equals("pickup"))
            {
                //TODO: pick up synchronisation
            }
            else if(type.equals("spawn"))
            {
                //TODO: new pickup registration
            }
            else if(type.equals("confirm_pickup"))
            {
                //TODO: pick up confirmation
            }
            else if(type.equals("sync"))
            {
                networking.setTimestamp(realTimeMessage.getSenderParticipantId());
            }
            else if(type.equals("ping"))
            {
                networking.pingCorrection(json.getInt("ping"), realTimeMessage.getSenderParticipantId());
            }
            else if(type.equals("start"))
            {
                networking.startTimer(json.getLong("timestamp"));
            }
            else if(type.equals("self"))
            {
                //Do nothing, it's a search for self ID
            }
            else
            {
                Log.wtf("message_error", "wtf are you???");
                Log.wtf("message_error", json.toString());
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return;
        }
    }



}
