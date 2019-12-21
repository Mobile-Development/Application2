package com.example.myapplication.ui.weather;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MyLocationData;

public class MyLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        double latitude = location.getLatitude();    //获取纬度信息
        double longitude = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f
        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
        int errorCode = location.getLocType();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

    }
}