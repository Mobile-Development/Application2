package com.example.myapplication.data;

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
    private EditText text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_health);
        getSupportActionBar().hide();
        confirm_button = findViewById(R.id.health_confirm);
        text2 = findViewById(R.id.health_text2);
        confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(HealthRecord.this, text2.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
