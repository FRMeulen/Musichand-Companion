package com.example.falco.musichandcompanion;

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

    //Button
    Button selectButton;
    Button selectRightDeviceButton;
    Button selectLeftDeviceButton;

    //TextViews
    TextView selectedInstrument;
    TextView selectedRightDevice;
    TextView selectedLeftDevice;

    //Connection services
    BluetoothConnectionService rightService;
    BluetoothConnectionService leftService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedInstrument = findViewById(R.id.selectedInstrument);
        selectedRightDevice = findViewById(R.id.selectedRightBT);
        selectedLeftDevice = findViewById(R.id.selectedLeftBT);

        if(savedInstanceState != null){
            selectedInstrument.setText(savedInstanceState.getString("instrument"));
            rightService = (BluetoothConnectionService)savedInstanceState.getSerializable("rightConnection");
            selectedRightDevice.setText(savedInstanceState.getString("rightConnectionName"));
            leftService = (BluetoothConnectionService)savedInstanceState.getSerializable("leftConnection");
            selectedLeftDevice.setText(savedInstanceState.getString("leftConnectionName"));
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            selectedInstrument.setText(extras.getString("instrument"));
            rightService = (BluetoothConnectionService)extras.getSerializable("rightConnection");
            selectedRightDevice.setText(extras.getString("rightConnectionName"));
            leftService = (BluetoothConnectionService)extras.getSerializable("leftConnection");
            selectedLeftDevice.setText(extras.getString("leftConnectionName"));
        }

        selectButton = findViewById(R.id.selectInstrumentButton);
        selectRightDeviceButton = findViewById(R.id.selectRightDeviceButton);
        selectLeftDeviceButton = findViewById(R.id.selectLeftDeviceButton);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectScreen();
            }
        });

        selectRightDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRightSelectionScreen();
            }
        });

        selectLeftDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLeftSelectionScreen();
            }
        });
    }

    public void toSelectScreen(){
        Intent selectIntent = new Intent(this, SelectionActivity.class);
        startActivity(selectIntent);
    }

    public void toRightSelectionScreen() {
        Intent selectIntent = new Intent(this, ConnectionActivity.class);
        selectIntent.putExtra("side", "right");
        startActivity(selectIntent);
    }

    public void toLeftSelectionScreen() {
        Intent selectIntent = new Intent(this, ConnectionActivity.class);
        selectIntent.putExtra("side", "left");
        startActivity(selectIntent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String instrument = savedInstanceState.getString("instrument");
        String rightConnectionName = savedInstanceState.getString("rightConnectionName");
        String leftConnectionName = savedInstanceState.getString("leftConnectionName");
        rightService = (BluetoothConnectionService)savedInstanceState.getSerializable("rightConnection");
        leftService = (BluetoothConnectionService)savedInstanceState.getSerializable("leftConnection");
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
        outState.putSerializable("rightConnection", rightService);
        outState.putSerializable("leftConnection", leftService);
    }
}
