package com.DrNankn.cleanwater.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class Report implements Parcelable {

    private final UUID mReportId;
    private final Date mTimeStamp;
    private final String mAuthorEmail;
    private LatLng mLocation;
    protected String mNotes;
    /**
     * Creates a Report
     */
    public Report(String authorEmail) {
        mReportId = UUID.randomUUID();
        mTimeStamp = new Date();
        mAuthorEmail = authorEmail;
    }

    public Report(String authorEmail, LatLng location) {
        this(authorEmail);
        mLocation = location;
    }

    protected Report(Parcel in) {
        mReportId = new UUID(in.readLong(), in.readLong());
        mTimeStamp = new Date(in.readLong());
        mAuthorEmail = in.readString();
        mLocation = in.readParcelable(LatLng.class.getClassLoader());
        mNotes = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mReportId.getMostSignificantBits());
        dest.writeLong(mReportId.getLeastSignificantBits());
        dest.writeLong(mTimeStamp.getTime());
        dest.writeString(mAuthorEmail);
        dest.writeParcelable(mLocation, flags);
        dest.writeString(mNotes);
    }

    public UUID getReportId() { return mReportId; }
    public Date getTimeStamp() { return mTimeStamp; }
    public String getAuthorEmail() { return mAuthorEmail; }
    public String getNotes() { return mNotes; }
    public void setNotes(String notes) { mNotes = notes; }
    public LatLng getLocation() { return mLocation; }
    public void setLocation(LatLng location) { mLocation = location; }

    @Override
    public String toString() {
        return "Report on " + new SimpleDateFormat("mm/dd").format(mTimeStamp) + " " + mReportId.toString();
    }
}
