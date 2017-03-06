package com.DrNankn.cleanwater.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.Models.WaterCondition;
import com.DrNankn.cleanwater.Models.WaterPurityReport;
import com.DrNankn.cleanwater.Models.WaterSourceReport;
import com.DrNankn.cleanwater.Models.WaterType;
import com.DrNankn.cleanwater.R;

public class NewWaterReportActivity extends AppCompatActivity {

    private User mActiveUser;
    private Spinner mWaterConditionSpinner;
    private Spinner mWaterTypeSpinner;
    private Button mAddLocationButton;
    private Button mCreateButton;
    private Button mCancelButton;
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
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWaterReportActivity.this.onBackPressed();
            }
        });
        mAddLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewWaterReportActivity.this, "Not available yet",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportType == R.layout.water_source_report)
                    createSourceReport();
                else if (reportType == R.layout.water_purity_report)
                    createPurityReport();
                MainActivity.mReports.add(mReport);
                finish();
            }
        });
    }

    private void createPurityReport() {
        String virusppm = ((EditText)findViewById(R.id.virus_ppm_text)).getText().toString();
        String contmppm = ((EditText)findViewById(R.id.contaminant_ppm_text)).getText().toString();
        WaterPurityReport report = new WaterPurityReport(
                mActiveUser.email,
                WaterCondition.valueOf(mWaterConditionSpinner.getSelectedItem().toString()),
                virusppm.equals("") ? 0 : Float.valueOf(virusppm),
                contmppm.equals("") ? 0 : Float.valueOf(contmppm)
        );
        mReport = report;
    }

    private void createSourceReport() {
        WaterSourceReport report = new WaterSourceReport(
                mActiveUser.email,
                WaterType.valueOf(mWaterTypeSpinner.getSelectedItem().toString()),
                WaterCondition.valueOf(mWaterConditionSpinner.getSelectedItem().toString())
        );
        mReport = report;
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
