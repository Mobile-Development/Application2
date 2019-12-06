package com.example.myapplication.Greendao;

import android.app.Application;

import com.example.myapplication.Greendao.entry.StepInfo;
import com.example.myapplication.Greendao.gen.DaoMaster;
import com.example.myapplication.Greendao.gen.DaoSession;
import com.example.myapplication.utils.DateUtil;


public class GreenDaoManager extends Application {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }
    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }


    private void init() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mInstance,
                "db");
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        insertData();
    }

    private void insertData(){
        StepInfo stepInfo = new StepInfo();
        stepInfo.setCreteTime(DateUtil.getTodayTime(DateUtil.DATE_FULL_STR));
        stepInfo.setDate(DateUtil.getTodayDate());
        stepInfo.setStepCount(300);
        stepInfo.setStepTotal(800);
        stepInfo.setPreviousStepCount(100);
        DBHelper.insertStepInfo(stepInfo);
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
