package com.example.myapplication.utils;

import com.example.myapplication.R;

import java.util.List;

public class TypeUtil {
    public static String indexToType(int index){
        switch (index){
            case 0:
                return "Swimming";
            case 1:
                return "Cycling";
            case 2:
                return  "Basketball";
            case 3:
                return "Tennis";
            case 4:
                return "Climbing";
            case 5:
                return "Running";
            case 6:
                return "Volleyball";
            case 7:
                return "Badminton";
            case 8:
                return "Skateboard";
            default:
                break;
        }
        return null;
    }
    public static int TypeToResourse(String type){
        switch (type){
            case "Swimming":
                return R.drawable.ic_swim_24dp;
            case "Cycling":
                return R.drawable.ic_cycling;
            case "Basketball":
                return R.drawable.ic_basketball ;
            case  "Tennis":
                return R.drawable.ic_tennis;
            case "Climbing":
                return R.drawable.ic_ski;
            case "Running":
                return R.drawable.ic_running;
            case "Volleyball":
                return R.drawable.ic_volleyball;
            case "Badminton":
                return R.drawable.sport_net;
            case "Skateboard":
                return R.drawable.ic_skateboard;
            default:
                break;
        }
        return 0;
    }
}
