package com.example.myapplication.constant;

public class Constant {
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;
    public static final int REQUEST_SERVER = 2;
    public static long TIME_INTERVAL = 500;
    public static final int BASE_STEP = 65;

    /**
     * 默认采集周期
     */
    public static final int DEFAULT_GATHER_INTERVAL = 5;
    /**
     * 默认打包周期
     */
    public static final int DEFAULT_PACK_INTERVAL = 15;
    /**
     * 实时定位间隔(单位:秒)
     */
    public static final int LOC_INTERVAL = 5;
    /**
     * 最后一次定位信息
     */
}
