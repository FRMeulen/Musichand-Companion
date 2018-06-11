package com.example.falco.musichandcompanion;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Debug tag
    String TAG = "MainActivity";
    String mode;    //String for play mode

    //Button
    Button selectButton;
    Button selectRightDeviceButton;
    Button selectLeftDeviceButton;
    Button playButton;

    //TextViews
    TextView selectedInstrument;
    TextView selectedRightDevice;
    TextView selectedLeftDevice;

    //Connection services
    BluetoothDevice rightDevice;
    BluetoothDevice leftDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedInstrument = findViewById(R.id.selectedInstrument);
        selectedRightDevice = findViewById(R.id.selectedRightBT);
        selectedLeftDevice = findViewById(R.id.selectedLeftBT);

        if(savedInstanceState != null){
            selectedInstrument.setText(savedInstanceState.getString("instrument"));
            rightDevice = savedInstanceState.getParcelable("rightDevice");
            selectedRightDevice.setText(savedInstanceState.getString("rightConnectionName"));
            leftDevice = savedInstanceState.getParcelable("leftDevice");
            selectedLeftDevice.setText(savedInstanceState.getString("leftConnectionName"));
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            selectedInstrument.setText(extras.getString("instrument"));
            rightDevice = extras.getParcelable("rightDevice");
            selectedRightDevice.setText(extras.getString("rightConnectionName"));
            leftDevice = extras.getParcelable("leftDevice");
            selectedLeftDevice.setText(extras.getString("leftConnectionName"));
        }

        selectButton = findViewById(R.id.selectInstrumentButton);
        selectRightDeviceButton = findViewById(R.id.selectRightDeviceButton);
        selectLeftDeviceButton = findViewById(R.id.selectLeftDeviceButton);
        playButton = findViewById(R.id.playButton);

        if(!selectedInstrument.getText().equals("NONE")){
            if(rightDevice != null && leftDevice != null){
                playButton.setVisibility(View.VISIBLE);
                mode = "two-handed";
            }

            else if(rightDevice != null && leftDevice == null){
                playButton.setVisibility(View.VISIBLE);
                mode = "right-handed";
            }

            else if(leftDevice != null && rightDevice == null){
                playButton.setVisibility(View.VISIBLE);
                mode = "left-handed";
            }
        }

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInstrumentSelectScreen();
            }
        });

        selectRightDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRightDeviceSelectionScreen();
            }
        });

        selectLeftDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLeftDeviceSelectionScreen();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPlayScreen();
            }
        });
    }

    public void toPlayScreen() {
        Intent playIntent = new Intent(this, PlayActivity.class);
        playIntent.putExtra("instrument", selectedInstrument.getText());
        playIntent.putExtra("rightDevice", rightDevice);
        playIntent.putExtra("leftDevice", leftDevice);
        playIntent.putExtra("mode", mode);
        startActivity(playIntent);
    }

    public void toInstrumentSelectScreen(){
        Intent selectIntent = new Intent(this, SelectionActivity.class);
        selectIntent.putExtra("rightConnectionName", selectedRightDevice.getText());
        selectIntent.putExtra("leftConnectionName", selectedLeftDevice.getText());
        selectIntent.putExtra("rightDevice", rightDevice);
        selectIntent.putExtra("leftDevice", leftDevice);
        startActivity(selectIntent);
    }

    public void toRightDeviceSelectionScreen() {
        Intent selectIntent = new Intent(this, ConnectionActivity.class);
        selectIntent.putExtra("instrument", selectedInstrument.getText());
        selectIntent.putExtra("side", "right");
        selectIntent.putExtra("leftDevice", leftDevice);
        selectIntent.putExtra("leftConnectionName", selectedLeftDevice.getText());

        startActivity(selectIntent);
    }

    public void toLeftDeviceSelectionScreen() {
        Intent selectIntent = new Intent(this, ConnectionActivity.class);
        selectIntent.putExtra("instrument",  selectedInstrument.getText());
        selectIntent.putExtra("side", "left");
        selectIntent.putExtra("rightDevice", rightDevice);
        selectIntent.putExtra("rightConnectionName", selectedRightDevice.getText());
        startActivity(selectIntent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String instrument = savedInstanceState.getString("instrument");
        String rightConnectionName = savedInstanceState.getString("rightConnectionName");
        String leftConnectionName = savedInstanceState.getString("leftConnectionName");
        rightDevice = savedInstanceState.getParcelable("rightDevice");
        leftDevice = savedInstanceState.getParcelable("leftDevice");
        selectedInstrument.setText(instrument);
        selectedRightDevice.setText(rightConnectionName);
        selectedLeftDevice.setText(leftConnectionName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String instrument = (String)selectedInstrument.getText();
        String rightConnectionName = (String)selectedRightDevice.getText();
        String leftConnectionName = (String)selectedLeftDevice.getText();
        outState.putString("instrument", instrument);
        outState.putString("rightConnectionName", rightConnectionName);
        outState.putString("leftConnectionName", leftConnectionName);
        outState.putParcelable("rightDevice", rightDevice);
        outState.putParcelable("leftDevice", leftDevice);
    }
}
