package com.DrNankn.cleanwater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] DUMMY_TEXTS = new String[]{
            "This activity intentionally left blank",
            "Creation of the rest of the app is left as an exercise to the user",
            "Great app, but too much water. 7.8/10 ~IGN"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.dummyText)).setText(
                DUMMY_TEXTS[new Random().nextInt(DUMMY_TEXTS.length)]);
        setTitle("Welcome " + getIntent().getStringExtra("USERNAME"));
        if (getIntent().getBooleanExtra("NEW_USER", false)) {
            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
