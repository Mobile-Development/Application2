package com.example.myapplication.utils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.Buffer;

import androidx.appcompat.app.AppCompatActivity;

public class PostGetUtil {


    /**
     * 使用post方式与服务器通讯
     * @param content
     * @return
     */
    public static String SendPostRequest(String content){
        HttpURLConnection conn=null;
        try {
            String Strurl="http://172.20.10.8:8888/user/login?id=111";
            URL url = new URL(Strurl);
            conn = (HttpURLConnection) url.openConnection();
            if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println(sb.toString());
                br.close();
            }else {
                Log.i("PostGetUtil","post请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return null;
    }

    /**
     * 使用get方式与服务器通信
     * @param content
     * @return
     */
    public static String SendGetRequest(String content){

        HttpURLConnection conn=null;
        try {

            String Strurl="http:";
            URL url = new URL(Strurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){
                Log.i("PostGetUtil","get请求成功");
                InputStream in=conn.getInputStream();
//                String backcontent=IOUtils.readString(in);
//                backcontent= URLDecoder.decode(backcontent,"UTF-8");
//                Log.i("PostGetUtil",backcontent);
                in.close();
            }
            else {
                Log.i("PostGetUtil","get请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            conn.disconnect();
        }
        return null;
    }
}