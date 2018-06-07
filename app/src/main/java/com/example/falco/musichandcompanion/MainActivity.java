package com.example.falco.musichandcompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Button
    Button selectButton;

    //TextViews
    TextView selectedInstrument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedInstrument = findViewById(R.id.selectedInstrument);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            selectedInstrument.setText(extras.getString("instrument"));
        }

        selectButton = findViewById(R.id.selectButton);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectScreen();
            }
        });
    }

    void toSelectScreen(){
        Intent selectIntent = new Intent(this, SelectionActivity.class);
        startActivity(selectIntent);
    }
}
