/* 
* This class represents different water conditions
* to be used in water reports
*/
package com.DrNankn.cleanwater.Models;

public enum WaterCondition {
    TreatableClear,
    Potable,
    Waste,
    TreatableMuddy;

    final public static String[] legalValues = new String[] {
            TreatableClear.toString(),
            Potable.toString(),
            Waste.toString(),
            TreatableMuddy.toString()
    };
}

