package com.DrNankn.cleanwater.Models;

/**
 * Created by nikhil on 3/6/2017.
 */

public class WaterSourceReport extends Report{

    private WaterCondition mWaterCondition;
    private WaterType mWaterType;

    /**
     * Creates a Water Source Report
     * @param type the water type of the Report
     * @param waterCondition the waterCondition of the water
     */
    public WaterSourceReport(String authorEmail, WaterType type, WaterCondition waterCondition) {
        super(authorEmail);
        mWaterType = type;
        mWaterCondition = waterCondition;
    }

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
