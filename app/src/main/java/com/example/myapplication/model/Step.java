package com.example.myapplication.model;


import java.util.Calendar;
import java.util.Date;

public class Step {
    private int stepNo;
    private java.util.Date Date;
    private int Id;
    private int StepCount;

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStepCount() {
        return StepCount;
    }

    public void setStepCount(int stepCount) {
        StepCount = stepCount;
    }
}
