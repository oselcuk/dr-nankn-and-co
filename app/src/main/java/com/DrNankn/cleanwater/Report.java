package com.DrNankn.cleanwater;

import android.os.Parcel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by nikhil on 3/6/2017.
 */

public class Report {

    String dateTime;
    String reportId;
    /**
     * Creates a Report
     */
    public Report() {
        dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Calendar.getInstance().getTime());;
        reportId = UUID.randomUUID().toString();
    }
}
