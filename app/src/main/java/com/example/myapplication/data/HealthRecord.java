package com.example.myapplication.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class HealthRecord extends AppCompatActivity {
    private TextView confirm_button;
    private TextView confirm_button2;
    private TextView marker;
    private EditText text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_health);
        getSupportActionBar().hide();
        confirm_button = findViewById(R.id.health_confirm);
        confirm_button2 = findViewById(R.id.health_confirm2);
        marker = findViewById(R.id.health_marker);
        text2 = findViewById(R.id.health_text2);
        confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(HealthRecord.this, text2.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        msetvisible();
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
}
