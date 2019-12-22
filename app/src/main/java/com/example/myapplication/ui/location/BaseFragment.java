package com.example.myapplication.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(getContentViewId());
    }

    /**
     * 获取布局文件ID
     */


    /**
     * 获取布局文件ID
     */
    public abstract int getContentViewId();

    /**
     * 设置Activity标题
     */
//    public void setTitle(int resId) {
//        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_top);
//        TextView textView = (TextView) layout.findViewById(R.id.tv_activity_title);
//        textView.setText(resId);
//    }

    /**
     * 设置点击监听器
//     */
//    public void setOnClickListener(View.OnClickListener listener) {
//        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_top);
//        LinearLayout optionsButton = (LinearLayout) layout.findViewById(R.id.btn_activity_options);
//        optionsButton.setOnClickListener(listener);
//    }

    /**
     * 不显示设置按钮
//     */
//    public void setOptionsButtonInVisible() {
//        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_top);
//        LinearLayout optionsButton = (LinearLayout) layout.findViewById(R.id.btn_activity_options);
//        optionsButton.setVisibility(View.INVISIBLE);
//    }

    /**
     * 回退事件
     */
    public void onBack(View v) {
        super.getActivity().onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
