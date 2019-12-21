package com.example.myapplication.ui.location;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.DistanceRequest;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.example.myapplication.R;
import com.example.myapplication.constant.Constant;
import com.example.myapplication.utils.CommonUtil;
import com.example.myapplication.utils.CurrentLocation;
import com.example.myapplication.utils.MapUtil;
import com.example.myapplication.utils.TrackReceiver;
import com.example.myapplication.utils.ViewUtil;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import static android.content.Context.SENSOR_SERVICE;

/**
 * 轨迹时时追踪
 */
public class TraceFragment extends BaseFragment implements View.OnClickListener, SensorEventListener {

    private TraceApplication traceApp = null;

    private ViewUtil viewUtil = null;

    private Button traceStartBtn = null;

    private TextView textDistance = null;

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private TrackReceiver trackReceiver = null;

    private SensorManager mSensorManager;

    private Double lastX = 0.0;
    private int mCurrentDirection = 0;

    /**
     * 地图工具
     */
    private MapUtil mapUtil = null;

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();

    private RealTimeLocRunnable realTimeLocRunnable = null;

    /**
     * 打包周期
     */
    public int packInterval = Constant.DEFAULT_PACK_INTERVAL;

    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints;

    private boolean firstLocate = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trace, container, false);
        initView(view);
        return inflater.inflate(R.layout.fragment_trace,container,false);
    }
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

//    public void setOptionsText() {
//        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_top);
//        TextView textView = (TextView) layout.findViewById(R.id.tv_options);
//        textView.setText("轨迹追踪设置");
//    }

    private void initView(View view) {
        viewUtil = new ViewUtil();
        mapUtil = MapUtil.getInstance();
        mapUtil.init((MapView) view.findViewById(R.id.bmapView));
        mapUtil.setCenter(mCurrentDirection);//设置地图中心点
        traceStartBtn = (Button) view.findViewById(R.id.buttonStart);
//        traceFinishBtn = (Button) getActivity().findViewById(R.id.buttonFinish);
        traceStartBtn.setOnClickListener(this);
//        traceFinishBtn.setOnClickListener(this);
         setTraceBtnStyle();
//        setGatherBtnStyle();
        textDistance = (TextView) view.findViewById(R.id.textDistance);


    }

    private void init(){
        traceApp = (TraceApplication) getActivity().getApplicationContext();
        trackPoints = new ArrayList<>();
        initListener();
        powerManager = (PowerManager) traceApp.getSystemService(Context.POWER_SERVICE);
        mSensorManager = (SensorManager) traceApp.getSystemService(SENSOR_SERVICE);// 获取传感器管理服务
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //每次方向改变，重新给地图设置定位数据，用上一次的经纬度
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {// 方向改变大于1度才设置，以免地图上的箭头转动过于频繁
            mCurrentDirection = (int) x;
            if (!CommonUtil.isZeroPoint(CurrentLocation.latitude, CurrentLocation.longitude)) {
                mapUtil.updateMapLocation(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude), (float) mCurrentDirection);
            }
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 追踪选项设置
//            case R.id.btn_activity_options:
//                ViewUtil.startActivityForResult(this, TracingOptionsActivity.class, Constant
//                        .REQUEST_CODE);
//                break;

            case R.id.buttonStart:
                if (traceApp.isTraceStarted) {
                    traceApp.mClient.stopGather(traceListener);
                    traceApp.mClient.stopTrace(traceApp.mTrace, traceListener);//停止服务
                } else {
                    traceApp.mClient.setInterval(Constant.DEFAULT_GATHER_INTERVAL, packInterval);
                    traceApp.mClient.startGather(traceListener);//开启采集
                    traceApp.mClient.startTrace(traceApp.mTrace, traceListener);//开始服务
                }
                break;
//
//            case R.id.btn_gather:
//                if (trackApp.isGatherStarted) {
//                    trackApp.mClient.stopGather(traceListener);
//                } else {
//                    trackApp.mClient.setInterval(Constant.DEFAULT_GATHER_INTERVAL, packInterval);
//                    trackApp.mClient.startGather(traceListener);//开启采集
//                }
//                break;

            default:
                break;
        }

    }

    /**
     * 设置服务按钮样式
     */
    private void setTraceBtnStyle() {
        boolean isTraceStarted = traceApp.trackConf.getBoolean("is_trace_started", false);
        if (isTraceStarted) {
            traceStartBtn.setText(R.string.stop_trace);
            traceStartBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                    .blue13, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                traceStartBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            } else {
                traceStartBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            }
        } else {
            traceStartBtn.setText(R.string.start_trace);
            traceStartBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.layout_title, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                traceStartBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            } else {
                traceStartBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            }
        }
    }

    /**
     * 设置采集按钮样式
//     */
//    private void setGatherBtnStyle() {
//        boolean isGatherStarted = trackApp.trackConf.getBoolean("is_gather_started", false);
//        if (isGatherStarted) {
//            traceFinishBtn.setText(R.string.stop_gather);
//            traceFinishBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                traceFinishBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_sure, null));
//            } else {
//                traceFinishBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_sure, null));
//            }
//        } else {
//            traceFinishBtn.setText(R.string.start_gather);
//            traceFinishBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.layout_title, null));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                traceFinishBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_cancel, null));
//            } else {
//                traceFinishBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_cancel, null));
//            }
////        }
//    }

    /**
     * 实时定位任务
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            traceApp.getCurrentLocation(entityListener, trackListener);
            realTimeHandler.postDelayed(this, interval * 1000);
        }
    }

    public void startRealTimeLoc(int interval) {
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
        traceApp.mClient.stopRealTimeLoc();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data) {
            return;
        }

        if (data.hasExtra("locationMode")) {
            LocationMode locationMode = LocationMode.valueOf(data.getStringExtra("locationMode"));
            traceApp.mClient.setLocationMode(locationMode);//定位模式
        }
        traceApp.mTrace.setNeedObjectStorage(false);

        if (data.hasExtra("gatherInterval") && data.hasExtra("packInterval")) {
            int gatherInterval = data.getIntExtra("gatherInterval", Constant.DEFAULT_GATHER_INTERVAL);
            int packInterval = data.getIntExtra("packInterval", Constant.DEFAULT_PACK_INTERVAL);
            TraceFragment.this.packInterval = packInterval;
            traceApp.mClient.setInterval(gatherInterval, packInterval);//设置频率
        }

    }

    private void initListener() {

        trackListener = new OnTrackListener() {
            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                //经过服务端纠偏后的最新的一个位置点，回调
                try {
                    if (StatusCodes.SUCCESS != response.getStatus()) {
                        return;
                    }

                    LatestPoint point = response.getLatestPoint();
                    if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                            .getLongitude())) {
                        return;
                    }

                    LatLng currentLatLng = MapUtil.convertTrace2Map(point.getLocation());
                    if (currentLatLng == null) {
                        return;
                    }

                    if(firstLocate){
                        firstLocate = false;
                        Toast.makeText(getContext(),"起点获取中，请稍后...",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //当前经纬度
                    CurrentLocation.locTime = point.getLocTime();
                    CurrentLocation.latitude = currentLatLng.latitude;
                    CurrentLocation.longitude = currentLatLng.longitude;

                    if (trackPoints == null) {
                        return;
                    }
                    trackPoints.add(currentLatLng);

                    mapUtil.drawHistoryTrack(trackPoints, false, mCurrentDirection);//时时动态的画出运动轨迹
                } catch (Exception x) {

                }


            }
            // 里程回调
            @Override
            public void onDistanceCallback(DistanceResponse response) {
                double distance = response.getDistance();//里程，单位：米
                if(distance!=0)
                textDistance.setText("您已步行 "+ distance+ "米。");
            }
        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                //本地LBSTraceClient客户端获取的位置
                try {
                    if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                            location.getLongitude())) {
                        return;
                    }
                    LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                    if (null == currentLatLng) {
                        return;
                    }
                    CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                    CurrentLocation.latitude = currentLatLng.latitude;
                    CurrentLocation.longitude = currentLatLng.longitude;

                    if (null != mapUtil) {
                        mapUtil.updateMapLocation(currentLatLng, mCurrentDirection);//显示当前位置
                        mapUtil.animateMapStatus(currentLatLng);//缩放
                    }

                } catch (Exception x) {

                }


            }

        };

        traceListener = new OnTraceListener() {

            @Override
            public void onBindServiceCallback(int errorNo, String message) {
                viewUtil.showToast(getActivity(),
                        String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    traceApp.isTraceStarted = true;
                    SharedPreferences.Editor editor = traceApp.trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
                    setTraceBtnStyle();
                    registerReceiver();
                }
                viewUtil.showToast(getActivity(),
                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    traceApp.isTraceStarted = false;
                    traceApp.isGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = traceApp.trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
                    setTraceBtnStyle();
//                    setGatherBtnStyle();
                    unregisterPowerReceiver();
                    firstLocate = true;
                }
                viewUtil.showToast(getActivity(),
                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    traceApp.isGatherStarted = true;
                    SharedPreferences.Editor editor = traceApp.trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();
//                    setGatherBtnStyle();
                    traceApp.mClient.queryDistance(traceApp.distanceRequest,trackListener);

                    stopRealTimeLoc();
                    startRealTimeLoc(packInterval);
                }
                viewUtil.showToast(getActivity(),
                        String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    traceApp.isGatherStarted = false;
                    SharedPreferences.Editor editor = traceApp.trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();
//                    setGatherBtnStyle();
                    firstLocate = true;
                    stopRealTimeLoc();
                    startRealTimeLoc(Constant.LOC_INTERVAL);
                    if (trackPoints.size() >= 1) {
                        try {
                            mapUtil.drawEndPoint(trackPoints.get(trackPoints.size() - 1));
                        } catch (Exception e) {
                        }
                    }

                }
                viewUtil.showToast(getActivity(),
                        String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

            }

            @Override
            public void onInitBOSCallback(int i, String s) {

            }
        };

    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 注册广播（电源锁、GPS状态）
     */
    @SuppressLint("InvalidWakeLockTag")
    private void registerReceiver() {
        if (traceApp.isRegisterReceiver) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        traceApp.registerReceiver(trackReceiver, filter);
        traceApp.isRegisterReceiver = true;

    }

    private void unregisterPowerReceiver() {
        if (!traceApp.isRegisterReceiver) {
            return;
        }
        if (null != trackReceiver) {
            traceApp.unregisterReceiver(trackReceiver);
        }
        traceApp.isRegisterReceiver = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (traceApp.trackConf.contains("is_trace_started")
                && traceApp.trackConf.contains("is_gather_started")
                && traceApp.trackConf.getBoolean("is_trace_started", false)
                && traceApp.trackConf.getBoolean("is_gather_started", false)) {
            startRealTimeLoc(packInterval);
        } else {
            startRealTimeLoc(Constant.LOC_INTERVAL);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapUtil.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);

        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = traceApp.getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRealTimeLoc();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRealTimeLoc();
        trackPoints.clear();
        trackPoints = null;
        mapUtil.clear();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_trace;
    }

}
