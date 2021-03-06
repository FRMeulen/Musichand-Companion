package com.example.falco.musichandcompanion;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class ParseThread extends Thread {

    private String dataRead;
    private SoundThread player;
    private final Context mContext;
    private static final String TAG = "ParseThread";

    public ParseThread(Context context){
        mContext = context;
    }

    public void run() {
        Log.i(TAG, "Created ParseThread");
        while(true){

        }
    }

    public void parse(String messageIn){
        if(messageIn.length() == 7){
            dataRead = messageIn.substring(0, 5);
            Log.d(TAG, "PlaySound called with "+dataRead);
            playSound(dataRead);
            dataRead = null;
        }

        else if (messageIn.length() < 7){
            if(dataRead == null){
                dataRead = messageIn;
                Log.d(TAG, "New length: "+dataRead.length());
            }
            else {
                dataRead += messageIn;
                Log.d(TAG, "Parsed message: "+dataRead+" with length "+dataRead.length());
                parse(dataRead);
            }
        }

        else {
            dataRead = null;
            Log.d(TAG, "Message too long, ignoring...");
        }
    }

    private void playSound(String sound){
        player = new SoundThread(sound, mContext);
    }
}