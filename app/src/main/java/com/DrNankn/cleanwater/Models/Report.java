/* 
* This class represents a report that holds information 
* about the author and the report itself
*/
package com.DrNankn.cleanwater.Models;

import android.annotation.SuppressLint;
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

    private double mLatitude;
    private double mLongitude;
    private String mNotes;
    /**
     * Creates a Report
     *
     * @param authorEmail   The author of the report's email
     */
    private Report(String authorEmail) {
        mReportId = UUID.randomUUID();
        mTimeStamp = new Date();
        mAuthorEmail = authorEmail;
    }

    /**
     * creates a report
     * @param authorEmail   The author of the report's email
     * @param location      The location for the report
     */
    Report(String authorEmail, LatLng location) {
        this(authorEmail);
        mLatitude = location.latitude;
        mLongitude = location.longitude;
    }

    Report(Parcel in) {
        mReportId = new UUID(in.readLong(), in.readLong());
        mTimeStamp = new Date(in.readLong());
        mAuthorEmail = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mNotes = in.readString();
    }

    Report() {this("");}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mReportId.getMostSignificantBits());
        dest.writeLong(mReportId.getLeastSignificantBits());
        dest.writeLong(mTimeStamp.getTime());
        dest.writeString(mAuthorEmail);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mNotes);
    }

    public UUID getReportId() { return mReportId; }
    public Date getTimeStamp() { return mTimeStamp; }
    public String getAuthorEmail() { return mAuthorEmail; }
    public String getNotes() { return mNotes; }
    public void setNotes(String notes) { mNotes = notes; }
    public double getLatitude() { return mLatitude; }
    public void setLatitude(double mLatitude) { this.mLatitude = mLatitude; }
    public double getLongitude() { return mLongitude; }
    public void setLongitude(double mLongitude) { this.mLongitude = mLongitude; }
    public LatLng getLocation() { return new LatLng(mLatitude, mLongitude); }

    /**
     * Checks if the report's location is in the requested range of latitude and longitude
     * @param lat the latitude for the requested range
     * @param lng the longitude for the requested range
     * @return boolean if the report is in the correct range
     */
    public boolean isInRequestedRange(float lat, float lng) {

        return this.getLatitude() < lat + 8
                && this.getLatitude() > lat - 8
                && this.getLongitude() < lng + 8
                && this.getLongitude() > lng - 8;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString() {
        return "Report on " + new SimpleDateFormat("mm/dd").format(mTimeStamp)
                + " " + mReportId.toString();
    }
}
