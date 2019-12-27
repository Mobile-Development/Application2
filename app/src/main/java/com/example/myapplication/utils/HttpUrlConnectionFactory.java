package com.example.myapplication.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
public class HttpUrlConnectionFactory{
    public static HttpURLConnection createConn(String cYurl) throws IOException {
        URL url = new URL(cYurl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
        return httpURLConnection;
    }
}
