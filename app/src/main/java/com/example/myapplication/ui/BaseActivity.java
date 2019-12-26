package com.example.myapplication.ui;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static volatile Activity mCurrentActivity;
    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }
    private void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }
    @Override
    protected void onResume() {
        super.onResume();
        setCurrentActivity(this);
    }

}
