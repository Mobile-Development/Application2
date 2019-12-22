package com.example.myapplication.ui.pedometer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;
import com.example.myapplication.Greendao.DBHelper;
import com.example.myapplication.Greendao.entry.StepInfo;
import com.example.myapplication.Greendao.entry.UserInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.constant.Constant;
import com.example.myapplication.stepDetector.StepService;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.utils.ConversionUtil;
import com.example.myapplication.utils.DateUtil;

import java.text.DecimalFormat;


import static android.content.Context.BIND_AUTO_CREATE;

public class HomeFragment extends Fragment implements Handler.Callback{

//    private HomeViewModel homeViewModel;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    private DecimalFormat df1 = new DecimalFormat("0.00");
    private TextView textView0, textView1, textView2;

    private LineChartView mChart;

    private final String[] mLabels = {"Sun", "Mon", "2", "3", "4", "5", "Sat"};

    private final float[] mValues = {800f, 7000f, 2000f, 13000f, 18000f, 4000f, 11000f};

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView0 = root.findViewById(R.id.stepcount);
        textView1 = root.findViewById(R.id.step);
        textView2 = root.findViewById(R.id.step2);
        mChart= root.findViewById(R.id.lineChart);

        initBarChatData();
        initData();
        initChart();
        return root;
    }



    private void initData() {
        delayHandler = new Handler(this);
        Intent intent = new Intent(getActivity(), StepService.class);
        getActivity().bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void initBarChatData() {
        StepInfo stepInfo = DBHelper.getStepInfo(DateUtil.getTodayDate());
        if (stepInfo != null) {
            long step = stepInfo.getStepCount();
            String mileages = String.valueOf(ConversionUtil.step2Mileage(step));
            String calorie = df1.format(ConversionUtil.step2Calories(step));
            textView0.setText(" " + step + " ");
            textView1.setText("   卡路里:" + calorie + "卡");
            textView2.setText("  行走:" + mileages + "米");
        }
    }

    private void initChart(){
        LineSet dataset = new LineSet(mLabels, mValues);
        dataset.setColor(Color.parseColor("#53c1bd"))
                .setFill(Color.parseColor("#3d6c73"))
                .setGradientFill(new int[] {Color.parseColor("#364d5a"), Color.parseColor("#3f7178")},
                        null)
                //.setThickness(4)
                .endAt(4);
        mChart.addData(dataset);

        mChart.setBorderSpacing(1)
                .setXLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setYLabels(AxisRenderer.LabelPosition.OUTSIDE);

        //Animation anim = new Animation().setEndAction(action);

        mChart.show();
    }



    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                long step = msg.getData().getLong("step");
                String mileages = String.valueOf(ConversionUtil.step2Mileage(step));
                String calorie = df1.format(ConversionUtil.step2Calories(step));
                textView0.setText("卡路里:" + calorie + "卡");
                textView2.setText("  行走:" + mileages + "米");
                delayHandler.sendEmptyMessageDelayed(Constant.REQUEST_SERVER, Constant.TIME_INTERVAL);
                //colorArcProgressBar.setCurrentValues((int) step);
                break;
            case Constant.REQUEST_SERVER:
                try {
                    Message msgl = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                    msgl.replyTo = mGetReplyMessenger;
                    messenger.send(msgl);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }
}