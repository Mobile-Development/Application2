package com.example.myapplication.ui.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.trace.api.entity.OnEntityListener;
import com.example.myapplication.R;
import com.example.myapplication.utils.HttpUrlConnectionFactory;
import com.example.myapplication.utils.JsonUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class WeatherFragment extends Fragment {

    private EditText locationET;
    private ImageButton searchBut;
    private ImageButton returnBut;
    private TextView currentWea;

    private Handler handler;

    private static final int SUCCESS = 0;
    private static final int NETWORK_ERROR = 10;
    private static final int API_ERROR = 20;
    private static final int LOC_UNKNOWN = 30;
    private OnEntityListener entityListener = null;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trace, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        mLocationClient.setLocOption(optionConfig());
        //mLocationClient为第二步初始化过的LocationClient对象
        initHandler();
        getActivity().setContentView(R.layout.fragment_weather);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void initView(View view) {
        currentWea = view.findViewById(R.id.w_currentWea);
        currentWea.setMovementMethod(ScrollingMovementMethod.getInstance());
        searchBut = view.findViewById(R.id.w_search);
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWea();

            }
        });

//        returnBut = findViewById(R.id.w_return);
//        returnBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(WeatherActivity.this, ChooseActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCCESS:
                        currentWea.setTextSize(22.0f);
                        currentWea.setText((String)msg.obj);
                        break;
                    case NETWORK_ERROR:
                        Toast.makeText(getActivity().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                        break;
                    case API_ERROR:
                        Toast.makeText(getActivity().getApplicationContext(), "API异常", Toast.LENGTH_SHORT).show();
                        break;
                    case LOC_UNKNOWN:
                        Toast.makeText(getActivity().getApplicationContext(), "位置信息有误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void searchWea() {
        String location = locationET.getText().toString().trim();
        if(location.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "请输入位置信息", Toast.LENGTH_SHORT).show();
            return;
        }
        final Message message = new Message();
        final String Lurl = "https://api.jisuapi.com/geoconvert/addr2coord?address="+location+"&type=baidu&appkey=04fc9fb54e80e512";
        new Thread() {
            @Override
            public void run() {
                try {
                    String Curl = getLatAndLong(Lurl, message);
                    if(Curl == null) {
                        handler.sendMessage(message);
                    } else {
                        getMessage(Curl, message);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String getLatAndLong(String Lurl, Message message) throws Exception{
        HttpURLConnection httpURLConnection = HttpUrlConnectionFactory.createConn(Lurl);
        httpURLConnection.connect();
        int errorCode = httpURLConnection.getResponseCode();
        BufferedReader br = null;
        if(errorCode == 200) {
            br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String json = br.readLine();
            Map<String, Object> map = JsonUtils.LocJsonUtil(json);
            if((int)map.get("errorCode") != 0) {
                message.what = 40;
                br.close();
                return null;
            }
            String latitude = (String)map.get("latitude");
            String longitude = (String)map.get("longitude");
            br.close();
            return "https://api.caiyunapp.com/v2/TAkhjf8d1nlSlspN/" + longitude + "," + latitude + "/hourly?lang=en_US&hourlysteps=6";
        } else {
            message.what = 10;
            return null;
        }
    }

    private void getMessage(String CYurl, Message message) throws Exception{
        HttpURLConnection httpURLConnection = HttpUrlConnectionFactory.createConn(CYurl);
        httpURLConnection.connect();

        int errorCode = httpURLConnection.getResponseCode();
        BufferedReader br = null;
        if(errorCode == 200) {
            br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String json = br.readLine();
            System.out.println(json);
            Map<String, Object> map = JsonUtils.CYTQJsonUtil(json);
            if((int)map.get("errorCode") == 10) {
                message.what = 20;
            } else {
                message.what = 0;
                message.obj = map.get("WeaInfo");
            }
            br.close();
            httpURLConnection.disconnect();
        } else {
            message.what = 10;
        }
        handler.sendMessage(message);
    }
    private LocationClientOption optionConfig(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setScanSpan(0);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(false);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        return  option;
    }




}

