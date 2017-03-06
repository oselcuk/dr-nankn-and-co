package com.DrNankn.cleanwater.Models;

import android.os.Parcel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by nikhil on 3/6/2017.
 */

public class Report {

    private final UUID mReportId;
    private final Date mTimeStamp;
    private final String mAuthorEmail;
    protected String mNotes;
    /**
     * Creates a Report
     */
    public Report(String authorEmail) {
        mReportId = UUID.randomUUID();
        mTimeStamp = new Date();
        mAuthorEmail = authorEmail;
    }

    public UUID getReportId() { return mReportId; }
    public Date getTimeStamp() { return mTimeStamp; }
    public String getAuthorEmail() { return mAuthorEmail; }
    public String getNotes() { return mNotes; }
    public void setNotes(String notes) { mNotes = notes; }

    @Override
    public String toString() {
        return "Report on " + new SimpleDateFormat("mm/dd").format(mTimeStamp) + " " + mReportId.toString();
    }
}
