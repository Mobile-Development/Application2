package com.example.myapplication.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class DialogActivity extends Activity {

    private TextView textView1;
    private Button button;
    private ImageView image;
    private Integer num1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        Integer kind = getIntent().getIntExtra("kind",0);
        String data = getIntent().getStringExtra("data");
        textView1=(TextView)findViewById(R.id.textView1);
        textView1.setText(data);
        button = findViewById(R.id.dialog_confirm);
        image = findViewById(R.id.dialog_icon);



        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("data", textView1.getText().toString());
                setResult(1, i);
                finish();
            }
        });
    }

    public void iv_1(View view){
        num1=Integer.parseInt(textView1.getText().toString());
        if(num1>1){
            num1-=1;
        }
        textView1.setText(Integer.toString(num1));

    }
    public void iv_2(View view){
        num1=Integer.parseInt(textView1.getText().toString());
        if(num1<999){
            num1+=1;
        }
        textView1.setText(Integer.toString(num1));
    }

}
