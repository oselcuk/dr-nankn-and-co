package com.DrNankn.cleanwater.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.Models.WaterCondition;
import com.DrNankn.cleanwater.Models.WaterPurityReport;
import com.DrNankn.cleanwater.Models.WaterSourceReport;
import com.DrNankn.cleanwater.Models.WaterType;
import com.DrNankn.cleanwater.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

public class NewWaterReportActivity extends AppCompatActivity {

    private User mActiveUser;
    private Spinner mWaterConditionSpinner;
    private Spinner mWaterTypeSpinner;
    private Button mAddLocationButton;
    private Button mCreateButton;
    private Button mCancelButton;
    private LatLng mLocation;
    private Report mReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActiveUser = getIntent().getParcelableExtra("USER");
        final int reportType = getIntent().getIntExtra("REPORT_TYPE", 0);
        setContentView(reportType);
        if (reportType == R.layout.water_source_report)
            setUpSourceReport();
        else if (reportType == R.layout.water_purity_report)
            setUpPurityReport();

        if (mWaterConditionSpinner != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, WaterCondition.legalValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mWaterConditionSpinner.setAdapter(adapter);
        }
        if (mWaterTypeSpinner != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, WaterType.legalValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mWaterTypeSpinner.setAdapter(adapter);
        }
        mCancelButton.setOnClickListener(v -> NewWaterReportActivity.this.onBackPressed());
        mAddLocationButton.setOnClickListener(v -> addLocation());
        mCreateButton.setOnClickListener(v -> {
            if (reportType == R.layout.water_source_report)
                createSourceReport();
            else if (reportType == R.layout.water_purity_report)
                createPurityReport();
            Intent result = new Intent();
            result.putExtra("REPORT", mReport);
            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }

    private void addLocation() {
        Random random = new Random();
        mLocation = new LatLng(random.nextDouble()*50-25, random.nextDouble()*50-25);
    }

    private void createPurityReport() {
        String virusppm = ((EditText)findViewById(R.id.virus_ppm_text)).getText().toString();
        String contmppm = ((EditText)findViewById(R.id.contaminant_ppm_text)).getText().toString();
        mReport = new WaterPurityReport(
                mActiveUser.email, mLocation,
                WaterCondition.valueOf(mWaterConditionSpinner.getSelectedItem().toString()),
                virusppm.equals("") ? 0 : Float.valueOf(virusppm),
                contmppm.equals("") ? 0 : Float.valueOf(contmppm)
        );
    }

    private void createSourceReport() {
        mReport = new WaterSourceReport(
                mActiveUser.email, mLocation,
                WaterType.valueOf(mWaterTypeSpinner.getSelectedItem().toString()),
                WaterCondition.valueOf(mWaterConditionSpinner.getSelectedItem().toString())
        );
    }

    private void setUpPurityReport() {
        setTitle("Submit purity report");
        TextView userLabel = (TextView) findViewById(R.id.purity_submitting_as_label);
        userLabel.append(" " + mActiveUser.name);
        mWaterConditionSpinner = (Spinner) findViewById(R.id.purity_water_condition_spinner);
        mAddLocationButton = (Button) findViewById(R.id.purity_add_location_button);
        mCreateButton = (Button) findViewById(R.id.purity_create_button);
        mCancelButton = (Button) findViewById(R.id.purity_cancel_button);
    }

    private void setUpSourceReport() {
        setTitle("Submit source report");
        TextView userLabel = (TextView) findViewById(R.id.source_submitting_as_label);
        userLabel.append(" " + mActiveUser.name);
        mWaterConditionSpinner = (Spinner) findViewById(R.id.source_water_condition_spinner);
        mWaterTypeSpinner = (Spinner) findViewById(R.id.source_type_spinner);
        mAddLocationButton = (Button) findViewById(R.id.source_add_location_button);
        mCreateButton = (Button) findViewById(R.id.source_create_button);
        mCancelButton = (Button) findViewById(R.id.source_cancel_button);
    }
}