package com.example.falco.musichandcompanion;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundThread extends Thread {

    private MediaPlayer mp;
    private Context mContext;

    SoundThread(String sound, Context context){
        mContext = context;

        if(sound.equals("mr:01")){
            mp = MediaPlayer.create(mContext, R.raw.maraca_1);
            this.start();
        }
    }

    public void run() {
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
}