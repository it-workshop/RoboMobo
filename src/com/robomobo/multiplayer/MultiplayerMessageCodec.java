package com.robomobo.multiplayer;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Всеволод on 30.03.2014.
 */
public class MultiplayerMessageCodec
{
    public static String encodeSeed(int seed) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "seed");
        json.put("type", seed);
        return json.toString();
    }

    public static String encodeReady() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "ready");
        return json.toString();
    }

    public static String encodeMove(float x, float y) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "move");
        json.put("x", x);
        json.put("y", y);
        return json.toString();
    }

    public static String encodePickUp(int id, long timestamp) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "pickup");
        json.put("id", id);
        json.put("timestamp", timestamp);
        return json.toString();
    }

    public static String encodeSpawn(int id, long timestamp) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "spawn");
        json.put("id", id);
        json.put("timestamp", timestamp);
        return json.toString();
    }

    public static String encodeConfirm(int id) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("type", "confirm_pickup");
        json.put("id", id);
        return json.toString();
    }

    public static void decode(String jsonString)
    {
        JSONObject json;
        try
        {
            json = new JSONObject(jsonString);
            String type = json.getString("type");
            if(type.equals("seed"))
            {
                //TODO: host choosing
            }
            else if(type.equals("ready"))
            {
                //TODO: count ready players
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
            else
            {
                Log.wtf("message_error", "wtf are you???");
                Log.wtf("message_error", jsonString);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return;
        }
    }
}
