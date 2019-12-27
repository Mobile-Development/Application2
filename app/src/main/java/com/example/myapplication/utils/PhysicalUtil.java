package com.example.myapplication.utils;

import com.example.myapplication.model.HealthData;



public class PhysicalUtil {
    public static float getBmi(){
        float bmi = HealthData.body_weight/((HealthData.body_height/100) * (HealthData.body_height/100));
        return bmi;
    }
    public static double getDpbf(){
        double Dpbf= (1.2*getBmi() + 0.23*HealthData.age - 5.4 - 10.8*HealthData.Male);
        return Dpbf;
    }
    public static double getDbmr(){
        Double Dbmr = (655 + (9.6*HealthData.body_weight) + 1.8* HealthData.body_height/100 - 4.7*HealthData.age) * HealthData.Male +
                (66 + (13.7*HealthData.body_weight) + 5* HealthData.body_height/100 - 6.8*HealthData.age) * HealthData.Female;
        return  Dbmr;
    }
    public static double getBmr(){
        Double bmr = getDbmr()/20;
        return bmr;
    }
}
