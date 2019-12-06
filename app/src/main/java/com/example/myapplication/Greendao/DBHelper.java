package com.example.myapplication.Greendao;

import android.widget.ListView;

import com.example.myapplication.Greendao.entry.StepInfo;
import com.example.myapplication.Greendao.entry.UserInfo;
import com.example.myapplication.Greendao.gen.StepInfoDao;
import com.example.myapplication.Greendao.gen.UserInfoDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class DBHelper {

    public static StepInfoDao getStepInfoDao() {
        return GreenDaoManager.getInstance().getmDaoSession().getStepInfoDao();
    }

    public static void insertStepInfo(StepInfo stepInfo) {
        if (stepInfo == null) {
            return;
        }
        StepInfo stepInfo1 = getStepInfo(stepInfo.getDate());
        if (stepInfo1 != null) {
            stepInfo.setId(stepInfo1.getId());
        }
        getStepInfoDao().insertOrReplace(stepInfo);
    }

    public static void updateStepInfo(StepInfo stepInfo) {
        if (stepInfo == null) {
            return;
        }
        getStepInfoDao().update(stepInfo);
    }

    public static StepInfo getStepInfo(String date) {
        Query<StepInfo> query = getStepInfoDao().queryBuilder().where(StepInfoDao.Properties.Date.eq(date)).build();
        return query.unique();
    }

    public static List<StepInfo> getAllStepInfo() {
        return getStepInfoDao().loadAll();
    }

    //-----------------------------------------------------
    public static UserInfoDao getUserInfoDao() {
        return GreenDaoManager.getInstance().getmDaoSession().getUserInfoDao();
    }

    public static void insertUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        getUserInfoDao().insertOrReplace(userInfo);
    }

    public static void updateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        getUserInfoDao().update(userInfo);
    }

    public static UserInfo getUserInfo() {
        //UserInfo userInfo = new UserInfo((long)11,"111","111");
        //getUserInfoDao().insert(userInfo);
        //return query.get(0);
        List<UserInfo> query = getUserInfoDao().loadAll();
        if (query.size() > 0) {
            return query.get(0);
        } else {
            return null;
        }
        //getUserInfoDao().deleteAll();
//        GreenDaoManager.getInstance().getmDaoSession().deleteAll(StepInfo.class);
//        GreenDaoManager.getInstance().getmDaoSession().deleteAll(UserInfo.class);

        //return null;
    }

}