package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.data.HealthRecord;
import com.example.myapplication.model.Account;
import com.example.myapplication.model.PersonInfo;
import com.example.myapplication.model.PersonSportsInfo;
import com.example.myapplication.model.Step;
import com.example.myapplication.ui.login.RegisterActivity;
import com.example.myapplication.ui.pedometer.HomeFragment;
import com.example.myapplication.ui.records.RecordsFragment;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DatabaseUtil {
    public static void LoginRequest(final Activity currentActivity ) {            //需要先将数据输入Account
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/LoginServlet";    //注①
        String tag = "Login";    //注②


        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(currentActivity.getApplicationContext());

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                String id = jsonObject.getString("userId");      //登录成功返回当前用户的id，根据id完成后续的操作
                                if (result.equals("success")) {  //注⑤
                                    //textView1.setText("Response is: "+ result+id);
                                    currentActivity.finish();
                                    Log.i("xsy login", "success");
                                } else {
                                    //做自己的登录失败操作，如Toast提示
                                    //Toast.makeText(getApplicationContext(), "Error:用户名或密码错误", Toast.LENGTH_LONG).show();
                                    //textView1.setText("Response is: "+ result);
                                    Log.i("xsy login", "unsuccess");
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
                params.put("AccountNumber", Account.getInstance().getAccountNumber());  //注⑥
                params.put("Password", Account.getInstance().getPassword());
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void RegisterRequest(final Activity activity) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/RegisterServlet";    //注①
        String tag = "Register";    //任意，只是标记的tag

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(activity.getApplicationContext());

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
                                    Intent intent = new Intent(RegisterActivity.getCurrentActivity(), HealthRecord.class);
                                    intent.putExtra("flag",false);
                                    activity.startActivity(intent);
                                    activity.finish();
                                } else {
                                    //做自己的登录失败操作，如Toast提示
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
                params.put("AccountNumber", Account.getInstance().getAccountNumber());  //注⑥
                params.put("Password", Account.getInstance().getPassword());
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void SearchPersonInfoRequest(final int Id,HomeFragment fragment) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/SearchPersonInfoServlet";    //注①
        String tag = "SearchPersonInfo";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity().getApplicationContext());

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                PersonInfo.getInstance().setHeight( (int)(Double.parseDouble(jsonObject.getString("Height"))*100));
                                PersonInfo.getInstance().setWeight((int)(Double.parseDouble(jsonObject.getString("Weight"))*10));
                                PersonInfo.getInstance().setBlood( Integer.parseInt(jsonObject.getString("Blood")));
                                PersonInfo.getInstance().setSitUpNumber( Integer.parseInt(jsonObject.getString("SitupNumber")));
                                PersonInfo.getInstance().setPushUpNumber( Integer.parseInt(jsonObject.getString("PushupNumber")));
                                PersonInfo.getInstance().setPullUp( Integer.parseInt(jsonObject.getString("PullUp")));
                                PersonInfo.getInstance().setAge( Integer.parseInt(jsonObject.getString("Age")));
                                PersonInfo.getInstance().setGender( Integer.parseInt(jsonObject.getString("Age")));
                                PersonInfo.getInstance().setHeartBeat( Integer.parseInt(jsonObject.getString("HeartBeat")));
                                if (result.equals("success")) {  //注⑤
                                    //
                                    Log.i("zxc personinfoGet","Success");

                                } else {
                                    //做自己的登录失败操作，如Toast提示

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
                params.put("Id", String.valueOf(Id));  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void UpdatePersonInfoRequest(Context context) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/UpdatePersonInfoServlet";    //注①
        String tag = "UpdatePersonInfo";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {  //注⑤


                                } else {
                                    //做自己的登录失败操作，如Toast提示

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
                params.put("Id", String.valueOf(PersonInfo.getInstance().getId()));
                params.put("Height", String.valueOf(PersonInfo.getInstance().getHeight()));
                params.put("Weight", String.valueOf(PersonInfo.getInstance().getWeight()));
                params.put("Blood", String.valueOf(PersonInfo.getInstance().getBlood()));
                params.put("SitupNumber", String.valueOf(PersonInfo.getInstance().getSitUpNumber()));
                params.put("PushupNumber", String.valueOf(PersonInfo.getInstance().getPushUpNumber()));
                params.put("PullUp", String.valueOf(PersonInfo.getInstance().getPullUp()));
                params.put("Age", String.valueOf(PersonInfo.getInstance().getAge()));
                params.put("Gender", String.valueOf(PersonInfo.getInstance().getGender()));
                params.put("HeartBeat", String.valueOf(PersonInfo.getInstance().getHeartBeat()));
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void SearchStepByIdRequest(final Step step, final int id, final HomeFragment fragment) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/QueryStepByIdServlet";    //注①
        String tag = "QueryStepById";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity().getApplicationContext());

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {  //注⑤
                                    String count = jsonObject.getString("Number");
                                    int counts=Integer.valueOf(count);
                                    String s="Response is: "+ result+counts;
                                    for(int i=0;i<counts;i++){
                                        String date=jsonObject.getString("Date"+i);
                                        if(date.equals("2019-12-26")){
                                            step.setStepCount(Integer.parseInt(jsonObject.getString("StepCountNumber"+i)));
                                            step.setDate(DateUtil.StringToDate(date,"yyyy-MM-dd"));
                                            step.setId(id);
                                        }
                                    }
                                    fragment.refreshView();
                                } else {
                                    //做自己的登录失败操作，如Toast提示
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
                params.put("Id", String.valueOf(id));  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }
    public static void InsertStepRequest(final Step step, Context context) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/InsertStepServlet";    //注①
        String tag = "InsertStep";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {


                                } else {
                                    //做自己的登录失败操作，如Toast提示

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
                params.put("Id", String.valueOf(step.getId()));
                String dateString = DateUtil.DateToString(step.getDate(),"yyyy-MM-dd");
                params.put("Date",dateString);
                params.put("StepCount",String.valueOf(step.getStepCount()));
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void UpdateStepRequest(final Step step,Context context) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/UpdateStepServlet";    //注①
        String tag = "UpdateStep";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {


                                } else {
                                    //做自己的登录失败操作，如Toast提示

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
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("Id", String.valueOf(step.getId()));
                String dateString = DateUtil.DateToString(step.getDate(),"yyyy-MM-dd");
                params.put("Date",dateString);
                params.put("StepCount",String.valueOf(step.getStepCount()));
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void SearchPersonSportsInfoByIdRequest(final List<PersonSportsInfo> personSportsInfos , final int id, final RecordsFragment fragment) {//目前到这
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/QueryPersonSportsInfoServlet";    //注①
        String tag = "QueryPersonSportsInfoById";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity().getApplicationContext());

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {  //注⑤
                                    String count = jsonObject.getString("Number");
                                    int counts=Integer.valueOf(count);
                                    Log.i("zxc","success"+count);
                                    for(int i=0;i<counts;i++){
                                        PersonSportsInfo personSportsInfo = new PersonSportsInfo();
                                        personSportsInfo.setDate(DateUtil.StringToDate(jsonObject.getString("Date"+i),"yyyy-MM-dd"));
                                        personSportsInfo.setDuration(Integer.parseInt(jsonObject.getString("Duration"+i)));
                                        personSportsInfo.setCalorie(jsonObject.getString("Calorie"+i));
                                        personSportsInfo.setType(jsonObject.getString("Type"+i).toString());
                                        personSportsInfos.add(personSportsInfo);
                                    }
                                    fragment.refreshList();
                                    Log.i("fragment refreshList","0");

                                } else {
                                    //做自己的登录失败操作，如Toast提示
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
                params.put("Id", String.valueOf(id));  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    public static void SearchPersonSportsInfoOnSomedayRequest(final int id,final String date,final List<PersonSportsInfo> personSportsInfos,  Context context) {//未解决
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/QueryPersonSportsInfoOnSomeDayServlet";    //注①
        String tag = "QueryPersonSportsInfoOnSomeDay";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {  //注⑤
                                    String count = jsonObject.getString("Number");
                                    int counts=Integer.valueOf(count);
                                    String s="Response is: "+ result+counts;
                                    for(int i=0;i<counts;i++){
                                        PersonSportsInfo personSportsInfo = new PersonSportsInfo();
                                        Date date = DateUtil.StringToDate(jsonObject.getString("Date"+i),"yyyy-MM-dd");
                                        personSportsInfo.setDate(date);
                                        personSportsInfo.setDuration(Integer.parseInt(jsonObject.getString("Duration"+i)));
                                        personSportsInfo.setCalorie(jsonObject.getString("Calorie"+i));
                                        personSportsInfo.setType(jsonObject.getString("Type"+i));
                                        personSportsInfo.setSNo(Integer.parseInt(jsonObject.getString("SNo"+i)));
                                        personSportsInfos.add(personSportsInfo);
                                    }
                                    //
                                } else {
                                    //做自己的登录失败操作，如Toast提示
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
                params.put("Id", String.valueOf(id));
                params.put("Date",date);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);

    }

    public static void InsertPersonSportsInfoRequest(final PersonSportsInfo personSportsInfo , final RecordsFragment fragment) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/InsertPersonSportsInfoServlet";    //注①
        String tag = "InsertPersonSportsInfo";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity().getApplicationContext());

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {
                                    fragment.refreshList();

                                } else {
                                    //做自己的登录失败操作，如Toast提示
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
                params.put("Id", String.valueOf(personSportsInfo.getId()));
                params.put("Date",DateUtil.DateToString(personSportsInfo.getDate(),"yyyy-MM-dd"));
                params.put("Duration",String.valueOf(personSportsInfo.getDuration()));
                params.put("Type",personSportsInfo.getType());
                params.put("Calorie",String.valueOf(personSportsInfo.getCalorie()));
                return params;
            }
        };

//        //设置Tag标签
//        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void DeletePersonSportsInfoRequest(final int sNo,Context context) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/DeletePersonSportsInfoServlet";    //注①
        String tag = "InsertPersonSportsInfo";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {
                                } else {
                                    //做自己的登录失败操作，如Toast提示
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
                params.put("SNo", String.valueOf(sNo));
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public static void UpdatePersonSportsInfoRequest(final PersonSportsInfo personSportsInfo,Context context) {
        //请求地址
        String url = "http://49.235.33.137:8080/myFirstWebApp/UpdatePersonSportsInfoServlet";    //注①
        String tag = "InsertPersonSportsInfo";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                String result = jsonObject.getString("Result");  //成功或者失败
                                if (result.equals("success")) {

                                } else {
                                    //做自己的登录失败操作，如Toast提示
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
                params.put("SNo", String.valueOf(personSportsInfo.getSNo()));
                params.put("Date",DateUtil.DateToString(personSportsInfo.getDate(),"yyyy-MM-dd"));
                params.put("Duration",String.valueOf(personSportsInfo.getDuration()));
                params.put("Type",personSportsInfo.getType());
                params.put("Calorie",String.valueOf(personSportsInfo.getCalorie()));
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
