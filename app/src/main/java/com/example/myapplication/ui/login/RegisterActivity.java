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
import com.example.myapplication.utils.DatabaseUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
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
                DatabaseUtil.RegisterRequest(getApplicationContext());
                //注册完成后
                Intent intent = new Intent(RegisterActivity.this, HealthRecord.class);
                intent.putExtra("flag",false);
                startActivity(intent);
                finish();

            }
        });
    }
    public void RegisterRequest(final String accountNumber, final String password) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/RegisterServlet";    //注①
        String tag = "Register";    //任意，只是标记的tag
        Log.i("xsy test", "Register");

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != "") {
                            try {
                                JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                                String result = jsonObject.getString("Result");  //注④
                                if (result.equals("success")) {  //注⑤
                                    Log.i("xsy test", "success");

                                } else {
                                    //做自己的登录失败操作，如Toast提示
                                    Log.i("xsy test", "wrong");
                                }
                            } catch (Exception e) {
                                //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                                Log.e("TAG", e.getMessage(), e);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("Password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
