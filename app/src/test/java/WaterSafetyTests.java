
/**
 * Created by Doug Gresham on 4/11/17
 *
 * JUnit tests for checking if water is safe
 **/

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import com.google.android.gms.maps.model.LatLng;
import com.DrNankn.cleanwater.Models.WaterPurityReport;
import com.DrNankn.cleanwater.Models.WaterCondition;

public class WaterSafetyTests {
    //test1
    @Test
    public void testIsWaterSafe1() {
        WaterPurityReport report = new WaterPurityReport("douggresham@gatech.edu", new LatLng(0, 0), WaterCondition.Waste, 0f, 0f);
        assertTrue("isWaterSafe() returns false for safe water", report.isWaterSafe());
    }

    //test 2
    @Test
    public void testIsWaterSafe2() {
        WaterPurityReport report = new WaterPurityReport("douggresham@gatech.edu", new LatLng(0, 0), WaterCondition.Waste, 3f, 3f);
        assertFalse("isWaterSafe() returns true for unsafe water", report.isWaterSafe());
    }

    //test 3
    @Test
    public void testIsWaterSafe3() {
        WaterPurityReport report = new WaterPurityReport("douggresham@gatech.edu", new LatLng(0, 0), WaterCondition.Waste, .0001f, .01f);
        assertFalse("isWaterSafe() returns true for water that is just above the proper level", report.isWaterSafe());
    }

    //test 4
    @Test
    public void testIsWaterSafe4() {
        WaterPurityReport report = new WaterPurityReport("douggresham@gatech.edu", new LatLng(0, 0), WaterCondition.Waste, 1f, .0001f);
        assertFalse("isWaterSafe() returns true for water with an unsafe virus PPM level", report.isWaterSafe());
    }

    //test 5
    @Test
    public void testIsWaterSafe5() {
        WaterPurityReport report = new WaterPurityReport("douggresham@gatech.edu", new LatLng(0, 0), WaterCondition.Waste, .00001f, 1f);
        assertFalse("isWaterSafe() returns true for water with an unsafe contaminate PPM level", report.isWaterSafe());
    }
}