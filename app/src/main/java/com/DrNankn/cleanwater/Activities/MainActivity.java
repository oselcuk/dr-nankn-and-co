package com.DrNankn.cleanwater.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int SUBMIT_REPORT_VALUE = 0;
    private static final int EDIT_PROFILE_VALUE = 1;
    private static List<Report> mReports = new ArrayList<>(); // Temporary reports store
    private DatabaseReference mDatabase;

    User mActiveUser;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Set active user and the title
        mActiveUser = getIntent().getParcelableExtra("USER");
        setTitle("Welcome " + mActiveUser.name + "(" + mActiveUser.role + ")");
        if (getIntent().getBooleanExtra("NEW_USER", false)) {
            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
        }

        // Set callbacks for the submit report button
        // TODO: Getting the list of possible report types should be delegated to the user object
        final Button submit_report = (Button) findViewById(R.id.submit_report_button);
        switch (mActiveUser.role) {
            case User:
                submit_report.setText(getString(R.string.add_water_source_report));
                submit_report.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, NewWaterReportActivity.class);
                    intent.putExtra("REPORT_TYPE", R.layout.water_source_report);
                    intent.putExtra("USER", mActiveUser);
                    startActivityForResult(intent, SUBMIT_REPORT_VALUE);
                });
                break;
            case Worker:
                submit_report.setOnClickListener(v -> {
                    PopupMenu popup = new PopupMenu(MainActivity.this, submit_report);
                    popup.getMenuInflater().inflate(R.menu.submit_report_popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(item -> {
                        Intent intent = new Intent(MainActivity.this, NewWaterReportActivity.class);
                        if (item.getItemId() == R.id.add_source_report) {
                            intent.putExtra("REPORT_TYPE", R.layout.water_source_report);
                        } else if (item.getItemId() == R.id.add_purity_report) {
                            intent.putExtra("REPORT_TYPE", R.layout.water_purity_report);
                        } else {
                            return false;
                        }
                        intent.putExtra("USER", mActiveUser);
                        startActivityForResult(intent, SUBMIT_REPORT_VALUE);
                        return true;
                    });
                    popup.show();
                });
                break;
            case Manager:
            case Administrator:
                submit_report.setOnClickListener(v -> {
                    PopupMenu popup = new PopupMenu(MainActivity.this, submit_report);
                    popup.getMenuInflater().inflate(R.menu.submit_report_popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(item -> {
                        Intent intent;
                        if (item.getItemId() == R.id.add_source_report) {
                            intent = new Intent(MainActivity.this, NewWaterReportActivity.class);
                            intent.putExtra("REPORT_TYPE", R.layout.water_source_report);
                        } else if (item.getItemId() == R.id.add_purity_report) {
                            intent = new Intent(MainActivity.this, NewWaterReportActivity.class);
                            intent.putExtra("REPORT_TYPE", R.layout.water_purity_report);
                        } else if (item.getItemId() == R.id.add_history_report) {
                            intent = new Intent(MainActivity.this, NewHistoricalReportActivity.class);
                            intent.putExtra("USER", mActiveUser);
                        } else {
                            return false;
                        }
                        intent.putExtra("USER", mActiveUser);
                        startActivityForResult(intent, SUBMIT_REPORT_VALUE);

                        return true;
                    });
                    popup.show();
                });
                break;
        }

        // Ask android for the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Add a location marker for the given report
     * @param report Report object to add a marker for
     */
    public void addLocationMarker(Report report) { // TODO: move all this map crap to a separate fragment?
        if (report.getLocation() == null) {
            Random random = new Random();
            LatLng mLocation = new LatLng(random.nextDouble()*50-25, random.nextDouble()*50-25);
            report.setLocation(mLocation);
        }
        mMap.addMarker(new MarkerOptions().position(report.getLocation()).title(report.toString()).draggable(true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("USER", mActiveUser);
                startActivityForResult(intent, EDIT_PROFILE_VALUE);
                return true;
            case R.id.action_log_out:
                finish();
                return true;
            case R.id.action_list_reports:
                intent = new Intent(this, ListReportsActivity.class);
                intent.putExtra("REPORTS", (ArrayList<Report>)mReports);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));
        mReports.forEach(this::addLocationMarker);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // TODO: Do things with location?
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SUBMIT_REPORT_VALUE:
                Report report = data.getParcelableExtra("REPORT");
                addLocationMarker(report);
                mReports.add(report);
                break;
            case EDIT_PROFILE_VALUE:
                mActiveUser = data.getParcelableExtra("USER");
                mDatabase.child("users").child(mActiveUser.email.replace('.', '_')).setValue(mActiveUser);
                break;
        }
    }
}
