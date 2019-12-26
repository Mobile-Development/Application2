package com.example.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.HealthRecord;
import com.example.myapplication.model.Account;
import com.example.myapplication.ui.BaseActivity;
import com.example.myapplication.ui.location.BaseFragment;
import com.example.myapplication.utils.DatabaseUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {
//    private EditText usernameEditText = findViewById(R.id.rusername);
//    private EditText passwordEditText = findViewById(R.id.rpassword);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        Button register = findViewById(R.id.rregister);
        final EditText username = findViewById(R.id.rusername);
        final EditText password = findViewById(R.id.rpassword);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account.getInstance().setAccountNumber(username.getText().toString());
                Account.getInstance().setPassword(password.getText().toString());
                DatabaseUtil.RegisterRequest(getCurrentActivity());
                //注册完成后


            }
        });
    }
}
