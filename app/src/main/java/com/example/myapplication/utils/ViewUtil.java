package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUtil {

    private Toast mToast = null;

    private TextView mTextView = null;

    public void showToast(Activity activity, String message) {
        StringBuilder strBuilder = new StringBuilder("<font face='" + activity.getString(R.string.font_type) + "'>");
        strBuilder.append(message).append("</font>");

        View toastRoot = activity.getLayoutInflater().inflate(R.layout.layout_toast, null);
        if (null == mToast || null == mTextView) {
            mToast = new Toast(activity);
            mToast.setView(toastRoot);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mTextView = toastRoot.findViewById(R.id.tv_toast_info);
            mTextView.setText(Html.fromHtml(strBuilder.toString()));
        } else {
            mTextView.setText(Html.fromHtml(strBuilder.toString()));
        }
        mToast.setGravity(Gravity.BOTTOM, 0, activity.getResources().getDisplayMetrics().heightPixels / 5);
        mToast.show();
    }
    /**
     * 调整Picker布局
     *
     * @param frameLayout
     */
    /**
     * 获取ViewGroup中的NumberPicker组件
     *
     * @param viewGroup
     *
     * @return
     */
    /**
     * 调整NumberPicker大小
     *
     * @param numberPicker
     */
    private static void resizeNumberPicker(NumberPicker numberPicker) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 15, 0);
        numberPicker.setLayoutParams(params);
    }

}
