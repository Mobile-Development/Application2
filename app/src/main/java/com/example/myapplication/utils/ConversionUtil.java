package com.example.myapplication.utils;

public class ConversionUtil {
    public static float step2Calories(long paramInt) {
        return (paramInt * 0.6f * 60 * 1.036f / 1000);
    }
//    public static float step2Calories(long paramInt, int benchmark) {
//        return (float) (paramInt * benchmark * 0.6D * 65.0D / 100000.0D);
//    }

    public static float step2Mileage(long paramInt) {
        return (paramInt * 0.6f);
    }

//    public static float step2Mileage(long paramInt, int benchmark) {
//        return (paramInt * benchmark / 100.0F);
//    }
}
