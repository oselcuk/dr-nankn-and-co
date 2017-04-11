/**
 * Created by nikhil on 4/11/2017.
 */

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.DrNankn.cleanwater.Models.Report;
import com.DrNankn.cleanwater.Models.WaterCondition;
import com.DrNankn.cleanwater.Models.WaterPurityReport;
import com.DrNankn.cleanwater.Models.WaterSourceReport;
import com.google.android.gms.maps.model.LatLng;
import com.DrNankn.cleanwater.Activities.LoginActivity;

public class ReportInRangeTest {

    WaterCondition condition = WaterCondition.Potable;
    Report report;

    /*
     * Test to determine if a password is valid or not
     */
    @Test
    public void isInRequestedRange() {
        /* Test 1 */
        report = new WaterPurityReport("tester@tester.com", new LatLng(5, 5), condition, 10, 20);
        assertTrue(report.isInRequestedRange(0, 0));
        /* Test 2 */
        report = new WaterPurityReport("tester2@tester.com", new LatLng(9, 9), condition, 30, 0);
        assertFalse(report.isInRequestedRange(0, 0));
        /* Test 3 */
        report = new WaterPurityReport("tester3@tester.com", new LatLng(-5,7), condition, 90, 200);
        assertTrue(report.isInRequestedRange(-1, 3));
        /* Test 4 */
        report = new WaterPurityReport("tester4@tester.com", new LatLng(11, -11), condition, 10, 20);
        assertFalse(report.isInRequestedRange(2, 2));
        /* Test 5 */
        report = new WaterPurityReport("tester5@tester.com", new LatLng(100, 91), condition, 10, 20);
        assertTrue(report.isInRequestedRange(95, 91));
    }
}
