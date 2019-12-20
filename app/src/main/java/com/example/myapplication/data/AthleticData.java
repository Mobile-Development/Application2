package com.example.myapplication.data;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AthleticData extends AppCompatActivity {
    private TextView confirm_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_health);
        getSupportActionBar().hide();
        confirm_button = findViewById(R.id.health_confirm);
        confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(AthleticData.this, text2.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
