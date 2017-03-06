package com.DrNankn.cleanwater.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by nikhil on 3/6/2017.
 */
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
    public WaterPurityReport(String workerEmail, WaterCondition waterCondition,
                             float virusPPM, float contaminantPPM) {
        super(workerEmail);
        mWaterCondition = waterCondition;
        mVirusPPM = virusPPM;
        mContaminantPPM = contaminantPPM;
    }

    public float getmVirusPPM() {
        return mVirusPPM;
    }

    public void setmVirusPPM(float mVirusPPM) {
        this.mVirusPPM = mVirusPPM;
    }

    public float getmContaminantPPM() {
        return mContaminantPPM;
    }

    public void setmContaminantPPM(float mContaminantPPM) {
        this.mContaminantPPM = mContaminantPPM;
    }

    @Override
    public String toString() {
        return "Water Purity " + super.toString();
    }
}