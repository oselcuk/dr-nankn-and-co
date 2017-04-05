/* 
* This class represents a WaterPurityReport which is a type
* of report that hold information about water purity
*/
package com.DrNankn.cleanwater.Models;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;

public class WaterPurityReport extends Report {

    private WaterCondition mWaterCondition;
    private float mVirusPPM;
    private float mContaminantPPM;


    /**
     * Creates a Water Purity Report
     *
     * @param workerEmail       email of worker who submitted the Report
     * @param waterCondition    condition of the water
     * @param virusPPM          PPM virus levels in the water
     * @param contaminantPPM    PPM contaminant levels in the water
     */
    public WaterPurityReport(String workerEmail, LatLng location, WaterCondition waterCondition,
                             float virusPPM, float contaminantPPM) {
        super(workerEmail, location);
        mWaterCondition = waterCondition;
        mVirusPPM = virusPPM;
        mContaminantPPM = contaminantPPM;
    }

    protected WaterPurityReport(Parcel in) {
        super(in);
        mWaterCondition = WaterCondition.valueOf(in.readString());
        mVirusPPM = in.readFloat();
        mContaminantPPM = in.readFloat();
    }

    private WaterPurityReport() {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mWaterCondition.name());
        dest.writeFloat(mVirusPPM);
        dest.writeFloat(mContaminantPPM);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WaterPurityReport> CREATOR = new Creator<WaterPurityReport>() {
        @Override
        public WaterPurityReport createFromParcel(Parcel in) {
            return new WaterPurityReport(in);
        }

        @Override
        public WaterPurityReport[] newArray(int size) {
            return new WaterPurityReport[size];
        }
    };

    public float getVirusPPM() {
        return mVirusPPM;
    }

    public void setVirusPPM(float mVirusPPM) {
        this.mVirusPPM = mVirusPPM;
    }

    public float getContaminantPPM() {
        return mContaminantPPM;
    }

    public void setContaminantPPM(float mContaminantPPM) {
        this.mContaminantPPM = mContaminantPPM;
    }

    public WaterCondition getWaterCondition() {
        return mWaterCondition;
    }

    public void setWaterCondition(WaterCondition waterCondition) {
        mWaterCondition = waterCondition;
    }

    @Override
    public String toString() {
        return "Water Purity " + super.toString();
    }
}