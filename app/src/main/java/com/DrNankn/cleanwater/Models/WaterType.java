/* 
* This class represents a different types of water
*/
package com.DrNankn.cleanwater.Models;

public enum WaterType {
    Bottled,
    Well,
    Lake,
    Stream,
    Other;

    final public static String[] legalValues = new String[] {
            Bottled.toString(),
            Well.toString(),
            Lake.toString(),
            Stream.toString(),
            Other.toString()
    };
}