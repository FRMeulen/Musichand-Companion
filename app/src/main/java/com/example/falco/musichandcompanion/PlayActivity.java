package com.example.falco.musichandcompanion;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    //Button
    Button resetButton;

    //TextView
    TextView playmode;

    //Objects for playing
    BluetoothConnectionService rightService;
    BluetoothConnectionService leftService;

    BluetoothDevice rightDevice;
    BluetoothDevice leftDevice;

    String instrument;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        resetButton = findViewById(R.id.resetButton);
        playmode = findViewById(R.id.play_mode);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightService != null){
                    rightService.killClient();
                }
                if(leftService != null){
                    leftService.killClient();
                }
                restartApp();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            rightDevice = extras.getParcelable("rightDevice");
            leftDevice = extras.getParcelable("leftDevice");
            instrument = extras.getString("instrument");
            mode = extras.getString("mode");
        }

        if(mode.equals("two-handed")){
            playmode.setText(R.string.two_handed);

            //Right connection
            rightService = new BluetoothConnectionService(this, "Right", this);
            rightService.startClient(rightDevice);

            //Left connection
            leftService = new BluetoothConnectionService(this, "Left", this);
            leftService.startClient(leftDevice);
        }

        else if(mode.equals("left-handed")){
            playmode.setText(R.string.left_handed);
            //Left connection
            leftService = new BluetoothConnectionService(this, "Left", this);
            leftService.startClient(leftDevice);
        }

        else if(mode.equals("right-handed")){
            playmode.setText(R.string.right_handed);
            //Right connection
            rightService = new BluetoothConnectionService(this, "Right", this);
            rightService.startClient(rightDevice);
        }
    }

    public void restartApp(){
        Intent restartIntent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(restartIntent);
    }
}
