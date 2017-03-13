package com.DrNankn.cleanwater.Models;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;

public class WaterSourceReport extends Report {

    private WaterCondition mWaterCondition;
    private WaterType mWaterType;

    /**
     * Creates a Water Source Report
     * @param type the water type of the Report
     * @param waterCondition the waterCondition of the water
     */
    public WaterSourceReport(String authorEmail, LatLng location, WaterType type, WaterCondition waterCondition) {
        super(authorEmail, location);
        mWaterType = type;
        mWaterCondition = waterCondition;
    }

    protected WaterSourceReport(Parcel in) {
        super(in);
        mWaterCondition = WaterCondition.valueOf(in.readString());
        mWaterType = WaterType.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mWaterCondition.name());
        dest.writeString(mWaterType.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WaterSourceReport> CREATOR = new Creator<WaterSourceReport>() {
        @Override
        public WaterSourceReport createFromParcel(Parcel in) {
            return new WaterSourceReport(in);
        }

        @Override
        public WaterSourceReport[] newArray(int size) {
            return new WaterSourceReport[size];
        }
    };

    public WaterCondition getmWaterCondition() {
        return mWaterCondition;
    }

    public void setmWaterCondition(WaterCondition mWaterCondition) {
        this.mWaterCondition = mWaterCondition;
    }

    public WaterType getmWaterType() {
        return mWaterType;
    }

    public void setmWaterType(WaterType mWaterType) {
        this.mWaterType = mWaterType;
    }

    @Override
    public String toString() {
        return "Water Content " + super.toString();
    }
}