package com.example.myapplication.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    //将地点转化为经纬度
    public static Map<String, Object> LocJsonUtil(String json) throws Exception {
        Map<String, Object> map = new HashMap<>();
        JSONArray jsonArray = new JSONArray("[" + json + "]");
        JSONObject root = jsonArray.getJSONObject(0);
        if(root.has("status") && root.getInt("status") != 0) {
            map.put("errorCode", 40);
            return map;
        } else {
            map.put("errorCode", 0);
        }
        if(root.has("result")) {
            JSONObject result = root.getJSONObject("result");
            if(result.has("lat")) {
                String latitude = String.valueOf(result.getDouble("lat"));
                map.put("latitude", latitude);
            }
            if(result.has("lng")) {
                String longitude = String.valueOf(result.getDouble("lng"));
                map.put("longitude", longitude);
            }
        }
        return map;
    }

    //解析天气JSON数据
    public static Map<String, Object> CYTQJsonUtil(String json) throws Exception {
        Map<String, Object> map = new HashMap<>();
        JSONArray jsonArray = new JSONArray("[" + json +"]");
        StringBuffer currentInfo = new StringBuffer();
        String currentSky = "";
        StringBuffer[] sb = null;
        //获取Json根对象
        JSONObject root = jsonArray.getJSONObject(0);
        if(!root.get("status").equals("ok")) {
            //status结果不为ok
            map.put("errorCode", 10);
            return map;
        }
        map.put("errorCode", 0);
        if(root.has("result")) {
            JSONObject result = root.getJSONObject("result");
            if(result.has("hourly")) {
                JSONObject hourly = result.getJSONObject("hourly");
                if(!hourly.get("status").equals("ok")) {
                    map.put("errorCode", 10);
                    return map;
                }
                map.put("hourly_status", 0);
                //气温
                if(hourly.has("temperature")) {
                    JSONArray temperatures = hourly.getJSONArray("temperature");
                    int length = temperatures.length();
                    sb = new StringBuffer[length-1];
                    JSONObject temperature = temperatures.getJSONObject(0);
                    if(temperature.has("datetime")) {
                        currentInfo.append("datetime: ").append(temperature.getString("datetime")).append("\n").append("\n");
                    }
                    if(temperature.has("value")) {
                        currentInfo.append("temperature: ").append(temperature.getDouble("value")).append("℃\n").append("\n");
                    }
                    for(int i = 1;i<length;i++) {
                        temperature = temperatures.getJSONObject(i);
                        if(temperature.has("value") && temperature.has("datetime")) {
                            if(sb[i-1] == null) {
                                sb[i-1] = new StringBuffer();
                            }
                            sb[i-1].append(temperature.getString("datetime")).append(" ").append(temperature.getString("value")).append("℃ ");
                        }
                    }
                }
                //天气现象
                if(hourly.has("skycon")) {
                    JSONArray skycons = hourly.getJSONArray("skycon");
                    int length = skycons.length();
                    if(sb == null) {
                        sb = new StringBuffer[length];
                    }
                    JSONObject skycon = skycons.getJSONObject(0);
                    if(skycon.has("value")) {
                        currentSky = skycon.getString("value");
                    }
                    for(int i = 1;i<length;i++) {
                        skycon = skycons.getJSONObject(i);
                        if(skycon.has("value")) {
                            if(sb[i-1] == null) {
                                sb[i-1] = new StringBuffer();
                            }
                            sb[i-1].append(skycon.getString("value")).append("\n");
                        }
                    }
                }
                //气压
                if(hourly.has("pres")) {
                    JSONObject pre = hourly.getJSONArray("pres").getJSONObject(0);
                    if(pre.has("value")) {
                        currentInfo.append("pres: ").append(pre.getDouble("value")).append("\n").append("\n");
                    }
                }
                //湿度
                if(hourly.has("humidity")) {
                    JSONObject humidity = hourly.getJSONArray("humidity").getJSONObject(0);
                    if(humidity.has("value")) {
                        currentInfo.append("humidity: ").append(humidity.getDouble("value")).append("\n").append("\n");
                    }
                }
                //能见度
                if(hourly.has("visibility")) {
                    JSONObject visibility = hourly.getJSONArray("visibility").getJSONObject(0);
                    if(visibility.has("value")) {
                        currentInfo.append("visibility: ").append(visibility.getDouble("value")).append("\n").append("\n");
                    }
                }
            }
        }
        StringBuffer temp = new StringBuffer("————————————————————————").append("\n");
        temp.append("                     ").append(currentSky).append("\n").append("\n");
        temp.append(currentInfo).append("————————————————————————").append("\n").append("             Next 5 Hours' Forecast\n").append("\n");
        int length = sb.length;
        for(int i = 0;i<length;i++) {
            temp.append(sb[i]).append("\n");
        }
        map.put("WeaInfo", temp.toString());
        return map;
    }
}
