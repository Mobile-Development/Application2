package com.example.myapplication.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.PersonInfo;
import com.example.myapplication.utils.DatabaseUtil;

public class AthleticData extends AppCompatActivity {
//    private TextView confirm_button;
//    private TextView confirm_button2;
//    private TextView marker;
    private TextView text_situp;
    private TextView text_pullup;
    private TextView text_pushup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_athletic);
        getSupportActionBar().hide();
        TextView confirm_button;
        TextView confirm_button2;
        TextView marker;
        confirm_button = findViewById(R.id.athletic_confirm);
        confirm_button2 = findViewById(R.id.athletic_confirm2);
        text_pushup = findViewById(R.id.text_pushUp);//pushup
        text_pullup = findViewById(R.id.text_pullUp);//pullup
        text_situp = findViewById(R.id.text_sitUP);//situp

        confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getAthleticData();
                finish();
            }
        });
        confirm_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getAthleticData();
                finish();
            }
        });
        mySetVisible(confirm_button, confirm_button2);

    }
    private void mySetVisible(TextView confirm_button, TextView confirm_button2){
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("flag",true);
        if(flag){
            confirm_button.setVisibility(View.VISIBLE);
            confirm_button2.setVisibility(View.GONE);
        }
        else{
            confirm_button.setVisibility(View.INVISIBLE);
            confirm_button2.setVisibility(View.VISIBLE);
        }
    }
    private void getAthleticData(){
        PersonInfo.getInstance().setPullUp(Integer.parseInt(text_pullup.getText().toString()));
        PersonInfo.getInstance().setPushUpNumber(Integer.parseInt(text_pushup.getText().toString()));
        PersonInfo.getInstance().setSitUpNumber(Integer.parseInt(text_situp.getText().toString()));
        DatabaseUtil.UpdatePersonInfoRequest(getApplicationContext());
    }
}
