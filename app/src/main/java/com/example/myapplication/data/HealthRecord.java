package com.example.myapplication.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.PersonInfo;
import com.example.myapplication.utils.DatabaseUtil;

public class HealthRecord extends AppCompatActivity {
    private TextView confirm_button;
    private TextView confirm_button2;
    private TextView marker;
    private EditText text1;
    private EditText text2;
    private EditText text3;
    private EditText text4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_health);
        getSupportActionBar().hide();

        confirm_button = findViewById(R.id.health_confirm);
        confirm_button2 = findViewById(R.id.health_confirm2);
        marker = findViewById(R.id.health_marker);
        text1 = findViewById(R.id.health_text1);//age

        text2 = findViewById(R.id.health_text2);//shengao
        text3 = findViewById(R.id.health_text3);//tizhong
        text4 = findViewById(R.id.health_text4);//xinlv

        confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getHealthData();
                finish();
            }
        });
        confirm_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getHealthData();
                Intent intent = new Intent(HealthRecord.this, AthleticData.class);
                intent.putExtra("flag",false);
                startActivity(intent);
                finish();
            }
        });
        msetvisible();

        RadioGroup rgroup=(RadioGroup)findViewById(R.id.radioSex);
        //通过setOnCheckedChangeListener( )来响应按钮的事
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup rg,int checkedId)
            {
                switch(checkedId){
                    case R.id.radioMale:{ PersonInfo.getInstance().setGender(1);break;}
                    case R.id.radioFemale: {PersonInfo.getInstance().setGender(0);break;}
                }
            }
        });

    }
    private void msetvisible(){
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("flag",true);
        if(flag){
            confirm_button.setVisibility(View.VISIBLE);
            marker.setVisibility(View.VISIBLE);
            confirm_button2.setVisibility(View.GONE);
        }
        else{
            confirm_button.setVisibility(View.INVISIBLE);
            confirm_button2.setVisibility(View.VISIBLE);
            marker.setVisibility(View.VISIBLE);
        }
    }
    private void getHealthData(){
        PersonInfo.getInstance().setAge(Integer.parseInt(text1.getText().toString()));
        PersonInfo.getInstance().setHeight(Integer.parseInt(text2.getText().toString()));
        PersonInfo.getInstance().setWeight(Integer.parseInt(text3.getText().toString()));
        PersonInfo.getInstance().setHeartBeat(Integer.parseInt(text4.getText().toString()));
        DatabaseUtil.UpdatePersonInfoRequest(getApplicationContext());
    }
}
