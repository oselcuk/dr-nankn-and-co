package com.DrNankn.cleanwater.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.Models.WaterPurityReport;
import com.DrNankn.cleanwater.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nikhil on 4/2/2017.
 */

public class NewHistoricalReportActivity extends AppCompatActivity {
    private User mActiveUser;

    private Spinner mPPMSpinner;
    private Button mCreateButton;
    private Button mCancelButton;
    private EditText mYear;
    private EditText mLatitude;
    private EditText mLongitude;
    private List<Report> purityReports = new ArrayList<Report>();
    private final String[] ppmType = {"Virus", "Contaminant"};
    private Map<Integer, ArrayList<Float>> ppmMap = new HashMap<Integer, ArrayList<Float>>();
    private int[] months = {0,1,2,3,4,5,6,7,8,9,10,11};
    private float[] ppm = new float[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_historical_report);
        mActiveUser = getIntent().getParcelableExtra("USER");
        setUpHistoryReport();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ppmType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPPMSpinner.setAdapter(adapter);
        mCancelButton.setOnClickListener(v -> NewHistoricalReportActivity.this.onBackPressed());
        mCreateButton.setOnClickListener(v -> {
            createPurityReportsList();
            createPurityReport();
            Intent intent = new Intent(NewHistoricalReportActivity.this, HistoricalReportGraph.class);
            intent.putExtra("USER", mActiveUser);
            intent.putExtra("PPM_ARR", ppm);
            intent.putExtra("MONTHS", months);
            intent.putExtra("PPM", mPPMSpinner.getSelectedItem().toString());
            startActivity(intent);
            finish();

        });
    }

    /**
     * Sets up the History Report parameters
     */
    private void setUpHistoryReport() {
        mPPMSpinner = (Spinner) findViewById(R.id.ppm_type_spinner);
        mCreateButton = (Button) findViewById(R.id.history_create_button);
        mCancelButton = (Button) findViewById(R.id.history_cancel_button);
        mLatitude = (EditText) findViewById(R.id.latitude3);
        mLongitude = (EditText) findViewById(R.id.longitude3);
        mYear = (EditText) findViewById(R.id.year);

    }
    /**
     * Creates a list of Purity Reports in a particular year and Region
     */
    private void createPurityReportsList() {
        float lat = mLatitude.getText().toString().equals("")? 0 : Float.valueOf(mLatitude.getText().toString());
        float lng = mLongitude.getText().toString().equals("")? 0 : Float.valueOf(mLongitude.getText().toString());
        int requestedYear = mYear.getText().toString().equals("")? 0: Integer.valueOf(mYear.getText().toString());
//        for (Report report : MainActivity.mReports) {
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(report.getTimeStamp());
//            int year = cal.get(Calendar.YEAR);
//            if (year == requestedYear) {
//                if (report instanceof WaterPurityReport) {
//                    if (report.getLocation().latitude < lat + 8
//                            && report.getLocation().latitude > lat - 8
//                            && report.getLocation().longitude < lng + 8
//                            && report.getLocation().longitude > lng - 8) {
//                        purityReports.add(report);
//                    }
//                }
//            }
//        }
    }
    /**
     * Calculates the average ppm per month value of the requested ppm value of all the valid reports
     */
    private void createPurityReport() {
        for (int i = 0; i < purityReports.size(); i++) {
            WaterPurityReport pReport = (WaterPurityReport)purityReports.get(i);
            Calendar cal = Calendar.getInstance();
            cal.setTime(pReport.getTimeStamp());
            int month = cal.get(Calendar.MONTH);
            ArrayList<Float> list = ppmMap.get(month);
            if (list == null) {
                list = new ArrayList<Float>();
                ppmMap.put(month, list);
            }
            if (mPPMSpinner.getSelectedItem().toString() == ppmType[0]) {
                list.add(pReport.getmVirusPPM());
            } else if (mPPMSpinner.getSelectedItem().toString() == ppmType[1]) {
                list.add(pReport.getmContaminantPPM());
            }
        }

        for (int i = 0; i < months.length; i++) {
            float sum = 0;
            int num = 0;
            if (ppmMap.get(i) != null) {
                for (float ppm : ppmMap.get(i)) {
                    sum += ppm;
                    num++;
                }
            }
            float avg = num != 0? sum/num: 0;
            ppm[i] = avg;
        }
    }
}
