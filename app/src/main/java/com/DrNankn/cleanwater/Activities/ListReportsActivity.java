package com.DrNankn.cleanwater.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.R;

import java.util.ArrayList;

public class ListReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reports);
        ArrayList<Report> reports = getIntent().getParcelableArrayListExtra("REPORTS");
        ListView list = (ListView) findViewById(R.id.reports_list_view);
        ArrayAdapter<Report> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, reports);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);
    }
}
