package com.example.myapplication.ui.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.utils.HttpUrlConnectionFactory;
import com.example.myapplication.utils.JsonUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class WeatherActivity extends AppCompatActivity {

    private EditText locationET;
    private ImageButton searchBut;
    private ImageButton returnBut;
    private TextView currentWea;

    private Handler handler;

    private static final int SUCCESS = 0;
    private static final int NETWORK_ERROR = 10;
    private static final int API_ERROR = 20;
    private static final int LOC_UNKNOWN = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();
        initHandler();
    }

    private void init() {
        locationET = findViewById(R.id.w_location);
        currentWea = findViewById(R.id.w_currentWea);
        currentWea.setMovementMethod(ScrollingMovementMethod.getInstance());


        searchBut = findViewById(R.id.w_search);
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
                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                        break;
                    case API_ERROR:
                        Toast.makeText(getApplicationContext(), "API异常", Toast.LENGTH_SHORT).show();
                        break;
                    case LOC_UNKNOWN:
                        Toast.makeText(getApplicationContext(), "位置信息有误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void searchWea() {
        String location = locationET.getText().toString().trim();
        if(location.equals("")) {
            Toast.makeText(getApplicationContext(), "请输入位置信息", Toast.LENGTH_SHORT).show();
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
}
