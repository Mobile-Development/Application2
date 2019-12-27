package com.example.myapplication.model;

public class HealthData {
    private static HealthData instance = new HealthData();
    public static  int age = 0;
    public static  int Male = 1;
    public static  int Female = 0;
    public static  int  body_height = 0;
    public static  int body_weight = 0;
    public static  int body_HR = 0;
    private HealthData(){};
    public static HealthData getInstance(){
        return instance;
    }
}
