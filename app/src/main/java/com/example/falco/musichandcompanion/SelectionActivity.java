package com.example.falco.musichandcompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {

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

        //OnClick Listeners
        bassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(bassButton.getText());
                bassButton.setText(">Bass<");
            }
        });

        celloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(celloButton.getText());
                celloButton.setText(">Cello<");
            }
        });

        djembeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(djembeButton.getText());
                djembeButton.setText(">Djembe<");
            }
        });

        drumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(drumsButton.getText());
                drumsButton.setText(">Drums<");
            }
        });

        guitarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(guitarButton.getText());
                guitarButton.setText(">Drums<");
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
                resetButtonStrings();
                selectedInstrument.setText(pianoButton.getText());
                pianoButton.setText(">Piano<");
            }
        });

        tambourineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(tambourineButton.getText());
                tambourineButton.setText(">Tambourine<");
            }
        });

        violinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonStrings();
                selectedInstrument.setText(violinButton.getText());
                violinButton.setText(">Violin<");
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(SelectionActivity.this, MainActivity.class);
                returnIntent.putExtra("instrument", selectedInstrument.getText());
                startActivity(returnIntent);
            }
        });
    }

    public void resetButtonStrings(){
        bassButton.setText("Bass");
        celloButton.setText("Cello");
        djembeButton.setText("Djembe");
        drumsButton.setText("Drums");
        guitarButton.setText("Guitar");
        maracasButton.setText("Maracas");
        pianoButton.setText("Piano");
        tambourineButton.setText("Tambourine");
        violinButton.setText("Violin");
    }
}
