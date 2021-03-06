/* 
* This class represents the activity of creating a new historical report
*/
package com.DrNankn.cleanwater.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.Models.WaterPurityReport;
import com.DrNankn.cleanwater.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NewHistoricalReportActivity extends AppCompatActivity {
    private static final int NUM_MONTHS = 12;
    private Spinner mPPMSpinner;
    private Button mCreateButton;
    private Button mCancelButton;
    private EditText mYear;
    private EditText mLatitude;
    private EditText mLongitude;
    private final List<Report> purityReports = new ArrayList<>();
    // Required to be ArrayList to avoid having to cast it to get in and out of intents
    @SuppressWarnings("CollectionDeclaredAsConcreteClass")
    private ArrayList<Report> mReports;
    private final String[] ppmType = {"Virus", "Contaminant"};
    private final List<List<Float>> ppmMap = new ArrayList<>(NUM_MONTHS);
    private final float[] ppm = new float[NUM_MONTHS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReports = getIntent().getParcelableArrayListExtra("REPORTS");
        setContentView(R.layout.water_historical_report);
        setUpHistoryReport();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ppmType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPPMSpinner.setAdapter(adapter);
        mCancelButton.setOnClickListener(v -> finish());
        mCreateButton.setOnClickListener(v -> showGraph());
    }

    /**
     * Generates the graph and replaces the current layout with it
     */
    private void showGraph() {
        createPurityReportsList();
        createPurityReport();

        setContentView(R.layout.historical_graph);
        String heading =
                "Historical Report Graph: " + mPPMSpinner.getSelectedItem().toString() + " ppm";
        TextView mHeading = (TextView) findViewById(R.id.heading);
        Button mClose = (Button) findViewById(R.id.close_graph);
        mHeading.setText(heading);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < NUM_MONTHS; i++) {
            series.appendData(new DataPoint(i+1,ppm[i]), true, NUM_MONTHS);
        }
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        graph.addSeries(series);
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        mClose.setOnClickListener(v -> finish());
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
        for (int j = 0; j < NUM_MONTHS; j++) {
            ppmMap.add(j, new ArrayList<>());
        }

    }
    /**
     * Creates a list of Purity Reports in a particular year and Region
     */
    private void createPurityReportsList() {
        float lat = "".equals(mLatitude.getText().toString()) ? 0
                : Float.valueOf(mLatitude.getText().toString());
        float lng = "".equals(mLongitude.getText().toString()) ? 0
                : Float.valueOf(mLongitude.getText().toString());
        int requestedYear = "".equals(mYear.getText().toString()) ? 0
                : Integer.valueOf(mYear.getText().toString());
        for (Report report : mReports) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(report.getTimeStamp());
            int year = cal.get(Calendar.YEAR);
            if (year == requestedYear) {
                if ((report instanceof WaterPurityReport) && report.isInRequestedRange(lat, lng)) {
                    purityReports.add(report);
                }
            }
        }
    }
    /**
     * Calculate the average ppm per month value of the requested ppm value of all the valid reports
     */
    private void createPurityReport() {
        for (int i = 0; i < purityReports.size(); i++) {
            WaterPurityReport pReport = (WaterPurityReport)purityReports.get(i);
            Calendar cal = Calendar.getInstance();
            cal.setTime(pReport.getTimeStamp());
            int month = cal.get(Calendar.MONTH);
            List<Float> list = ppmMap.get(month);
            if (list == null) {
                list = new ArrayList<>();
                ppmMap.add(month, list);
            }
            if (Objects.equals(mPPMSpinner.getSelectedItem().toString(), ppmType[0])) {
                list.add(pReport.getVirusPPM());
            } else if (Objects.equals(mPPMSpinner.getSelectedItem().toString(), ppmType[1])) {
                list.add(pReport.getContaminantPPM());
            }
        }

        for (int i = 0; i < NUM_MONTHS; i++) {
            float sum = 0;
            int num = 0;
            if (ppmMap.get(i) != null) {
                for (float ppm : ppmMap.get(i)) {
                    sum += ppm;
                    num++;
                }
            }
            float avg = (num != 0) ? (sum / num) : 0;
            ppm[i] = avg;
        }
    }
}
