package com.DrNankn.cleanwater;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] DUMMY_TEXTS = new String[]{
            "This activity intentionally left blank",
            "Creation of the rest of the app is left as an exercise to the user",
            "Great app, but too much water. 7.8/10 ~IGN"
    };

    User mActiveUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.dummyText)).setText(
                DUMMY_TEXTS[new Random().nextInt(DUMMY_TEXTS.length)]);
        mActiveUser = getIntent().getParcelableExtra("USER");
        setTitle("Welcome " + mActiveUser.name + "(" + mActiveUser.role + ")");
        if (getIntent().getBooleanExtra("NEW_USER", false)) {
            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("USER", mActiveUser);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
