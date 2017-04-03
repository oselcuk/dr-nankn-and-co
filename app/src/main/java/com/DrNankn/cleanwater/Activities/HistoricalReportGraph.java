package com.DrNankn.cleanwater.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by nikhil on 4/2/2017.
 */

public class HistoricalReportGraph extends AppCompatActivity {
    private User mActiveUser;
    private int[] mMonths;
    private float[] mPPM;
    private Button mClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historical_graph);
        mActiveUser = getIntent().getParcelableExtra("USER");
        mMonths = getIntent().getIntArrayExtra("MONTHS");
        mPPM = getIntent().getFloatArrayExtra("PPM_ARR");
        mClose = (Button) findViewById(R.id.close_graph);
        GraphView graph = null;
        if (mActiveUser != null) {
            graph = (GraphView) findViewById(R.id.graph);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < mMonths.length; i++) {
            series.appendData(new DataPoint(i+1,mPPM[i]), true, mMonths.length);
        }
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        graph.addSeries(series);
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        mClose.setOnClickListener(v -> {
            Intent intent2 = new Intent(HistoricalReportGraph.this, MainActivity.class);
            intent2.putExtra("USER", mActiveUser);
            startActivity(intent2);
        });

    }
}
