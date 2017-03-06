package com.DrNankn.cleanwater.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.Models.WaterPurityReport;
import com.DrNankn.cleanwater.Models.WaterSourceReport;
import com.DrNankn.cleanwater.Models.WaterType;
import com.DrNankn.cleanwater.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] DUMMY_TEXTS = new String[]{
            "This activity intentionally left blank",
            "Creation of the rest of the app is left as an exercise to the user",
            "Great app, but too much water. 7.8/10 ~IGN"
    };

    public static List<Report> mReports = new ArrayList<>(); // Temporary reports store

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

        final Button submit_report = (Button) findViewById(R.id.submit_report_button);
        if (mActiveUser.role == User.Role.User) {
            submit_report.setText(getString(R.string.add_water_source_report));
            submit_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, WaterSourceReport.class);
                    intent.putExtra("USER", mActiveUser);
                    startActivity(intent);
                }
            });
        } else {
            submit_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(MainActivity.this, submit_report);
                    popup.getMenuInflater().inflate(R.menu.submit_report_popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent = new Intent(MainActivity.this, NewWaterReportActivity.class);
                            if (item.getItemId() == R.id.add_source_report) {
                                intent.putExtra("REPORT_TYPE", R.layout.water_source_report);
                            } else if (item.getItemId() == R.id.add_purity_report) {
                                intent.putExtra("REPORT_TYPE", R.layout.water_purity_report);
                            } else {
                                return false;
                            }
                            intent.putExtra("USER", mActiveUser);
                            startActivity(intent);
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            ListView list = (ListView) findViewById(R.id.reports_list_view);
            ArrayAdapter<Report> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, mReports);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            list.setAdapter(adapter);
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
