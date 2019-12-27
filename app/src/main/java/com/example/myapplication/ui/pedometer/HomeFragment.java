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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;
import com.example.myapplication.Greendao.DBHelper;
import com.example.myapplication.Greendao.entry.StepInfo;
import com.example.myapplication.R;
import com.example.myapplication.constant.Constant;
import com.example.myapplication.model.Step;
import com.example.myapplication.stepDetector.StepService;
import com.example.myapplication.utils.ConversionUtil;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.DateUtil;

import java.text.DecimalFormat;


import static android.content.Context.BIND_AUTO_CREATE;

public class HomeFragment extends Fragment implements Handler.Callback{
    //    private HomeViewModel homeViewModel;

    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    private DecimalFormat df1 = new DecimalFormat("0.00");
    private TextView stepCountText, CalText, LengthText;
    private Step step;

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

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        stepCountText = root.findViewById(R.id.stepcount);
        CalText = root.findViewById(R.id.CAL_TEXT);
        LengthText = root.findViewById(R.id.LENGTH_TEXT);
        mChart= root.findViewById(R.id.lineChart);


        initData();
        initBarChatData();
        initChart();
        return root;
    }



    private void initData() {
        delayHandler = new Handler(this);
        Intent intent = new Intent(getActivity(), StepService.class);
        step= new Step();
        getActivity().bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void initBarChatData() {
//        StepInfo stepInfo = DBHelper.getStepInfo(DateUtil.getTodayDate());
        DatabaseUtil.SearchStepByIdRequest(step,1,this);
        refreshView();
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
                refreshView();
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
    public void refreshView(){
        if (step !=null) {
            int stepCount = step.getStepCount();
            int len = (int)(stepCount*0.4);
            String calorie = String.valueOf(len*1.5);
            stepCountText.setText(" " + stepCount + " ");
            CalText.setText("   卡路里:" + calorie + "卡");
            LengthText.setText("  行走:" + len + "米");
        }
    }
}