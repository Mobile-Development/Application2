package com.example.myapplication.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ui.BaseActivity;

public class DialogActivity extends BaseActivity {

    private TextView text_exeLength;
    private Button button;
    private ImageView image;
    private Integer num1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        String data = getIntent().getStringExtra("data");
        text_exeLength =(TextView)findViewById(R.id.text_exeLength);
        text_exeLength.setText(data);
        button = findViewById(R.id.dialog_confirm);
        image = findViewById(R.id.dialog_icon);



        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("exeLength", Integer.parseInt(text_exeLength.getText().toString()));
                setResult(1, i);
                finish();
            }
        });
    }

    public void iv_1(View view){
        num1=Integer.parseInt(text_exeLength.getText().toString());
        if(num1>1){
            num1-=1;
        }
        text_exeLength.setText(Integer.toString(num1));

    }
    public void iv_2(View view){
        num1=Integer.parseInt(text_exeLength.getText().toString());
        if(num1<999){
            num1+=1;
        }
        text_exeLength.setText(Integer.toString(num1));
    }

}
