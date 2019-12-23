package com.example.myapplication.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AthleticData extends AppCompatActivity {
//    private TextView confirm_button;
//    private TextView confirm_button2;
//    private TextView marker;

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
        marker = findViewById(R.id.athletic_marker);
        confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(AthleticData.this, text2.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        confirm_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(AthleticData.this, text2.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        msetvisible(confirm_button, confirm_button2);
    }
    private void msetvisible(TextView confirm_button, TextView confirm_button2){
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("flag",true);
        if(flag){
            confirm_button.setVisibility(View.VISIBLE);
            //marker.setVisibility(View.VISIBLE);
            confirm_button2.setVisibility(View.GONE);
        }
        else{
            confirm_button.setVisibility(View.INVISIBLE);
            confirm_button2.setVisibility(View.VISIBLE);
            //marker.setVisibility(View.VISIBLE);
        }
    }
}
