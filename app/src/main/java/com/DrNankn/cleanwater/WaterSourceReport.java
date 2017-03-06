package com.DrNankn.cleanwater;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by nikhil on 3/6/2017.
 */

public class WaterSourceReport extends Report{

    private String dateTime;
    private String reportId;
    private String[] WATERTYPE = {"Bottled", "Well", "Lake", "Stream", "Other"};
    private String type;
    private Condition condition;

    /**
     * Creates a Water Source Report
     */
    public WaterSourceReport() {
        super();
    }
    /**
     * Creates a Water Source Report
     * @param type the water type of the Report
     * @param condition the condition of the water
     */
    public WaterSourceReport(String type,Condition condition) {
        dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Calendar.getInstance().getTime());
        reportId = UUID.randomUUID().toString();
        this.type = type;
        this.condition = condition;
    }

    public enum Condition{
        TreatableClear,
        Potable,
        Waste,
        TreatableMuddy,
    }
}
