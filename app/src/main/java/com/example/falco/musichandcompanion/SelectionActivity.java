package com.example.falco.musichandcompanion;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {

    //Fields to return
    String rightConnectionName;
    String leftConnectionName;
    BluetoothDevice rightDevice;
    BluetoothDevice leftDevice;

    //Buttons
    Button bassButton;
    Button celloButton;
    Button djembeButton;
    Button drumsButton;
    Button guitarButton;
    Button maracasButton;
    Button pianoButton;
    Button tambourineButton;
    Button violinButton;
    Button doneButton;

    //TextView
    TextView selectedInstrument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        bassButton = findViewById(R.id.buttonInstrumentBass);
        celloButton = findViewById(R.id.buttonInstrumentCello);
        djembeButton = findViewById(R.id.buttonInstrumentDjembe);
        drumsButton = findViewById(R.id.buttonInstrumentDrums);
        guitarButton = findViewById(R.id.buttonInstrumentGuitar);
        maracasButton = findViewById(R.id.buttonInstrumentMaracas);
        pianoButton = findViewById(R.id.buttonInstrumentPiano);
        tambourineButton = findViewById(R.id.buttonInstrumentTambourine);
        violinButton = findViewById(R.id.buttonInstrumentViolin);
        doneButton = findViewById(R.id.doneButton);

        selectedInstrument = findViewById(R.id.instrumentSelection);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            rightConnectionName = extras.getString("rightConnectionName");
            leftConnectionName = extras.getString("leftConnectionName");
            rightDevice = extras.getParcelable("rightDevice");
            leftDevice = extras.getParcelable("leftDevice");
        }

        //OnClick Listeners
        bassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Bass");
            }
        });

        celloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Cello");
            }
        });

        djembeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Drums");
            }
        });

        drumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Drums");
            }
        });

        guitarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Guitar");
            }
        });

        maracasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(maracasButton.getText());
                maracasButton.setText(">Maracas<");
            }
        });

        pianoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Piano");
            }
        });

        tambourineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Tambourine");
            }
        });

        violinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWip("Violin");
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedInstrument.getText().equals("NONE")){
                    noneSelected();
                }
                else{
                    Intent returnIntent = new Intent(SelectionActivity.this, MainActivity.class);
                    returnIntent.putExtra("instrument", selectedInstrument.getText());
                    returnIntent.putExtra("rightConnectionName", rightConnectionName);
                    returnIntent.putExtra("leftConnectionName", leftConnectionName);
                    returnIntent.putExtra("rightDevice", rightDevice);
                    returnIntent.putExtra("leftDevice", leftDevice);
                    startActivity(returnIntent);
                }
            }
        });
    }

    public void showWip(String instrumentName){
        Toast.makeText(this, "Sorry! " + instrumentName + " is not available yet!", Toast.LENGTH_LONG).show();
    }

    public void noneSelected(){
        Toast.makeText(this, "No instrument selected!", Toast.LENGTH_LONG).show();
    }

    public void resetButtonStrings(){
        bassButton.setText(R.string.wip);
        celloButton.setText(R.string.wip);
        djembeButton.setText(R.string.wip);
        drumsButton.setText(R.string.wip);
        guitarButton.setText(R.string.wip);
        maracasButton.setText(R.string.instrument_maracas);
        pianoButton.setText(R.string.wip);
        tambourineButton.setText(R.string.wip);
        violinButton.setText(R.string.wip);
    }
}
