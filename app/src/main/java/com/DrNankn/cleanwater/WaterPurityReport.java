package com.DrNankn.cleanwater;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by nikhil on 3/6/2017.
 */
public class WaterPurityReport extends Report {

    private String dateTime;
    private String reportId;
    private String workerName;
    private Condition condition;
    private float virusPPM;
    private float contaminantPPM;

    /**
     * Creates a Water Purity Report
     */
    public WaterPurityReport() {
        super();
    }

    /**
     * Creates a Water Purity Report
     *
     * @param worker         the worker who submitted the Report
     * @param condition      the condition of the water
     * @param virusPPM       the PPM virus levels in the water
     * @param contaminantPPM the PPM contaminant levels in the water
     */
    public WaterPurityReport(String worker, Condition condition, float virusPPM, float contaminantPPM) {
        dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Calendar.getInstance().getTime());
        reportId = UUID.randomUUID().toString();
        this.workerName = worker;
        this.condition = condition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
    }

    public enum Condition {
        Safe,
        Treatable,
        Unsafe
    }
}