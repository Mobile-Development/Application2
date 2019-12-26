package com.example.myapplication.stepDetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 利用加速度传感器进行计步
 * */
public class StepDetector implements SensorEventListener {
    private final String TAG = "StepDetector";
    //存放三轴数据(x,y,z)的个数
    private final int valueNum = 5;
    //用于存放计算阈值的波峰波谷差值
    private float[] tempValue = new float[valueNum];
    private int tempCount = 0;
    //是否上升的标志位
    private boolean isDirectionUp = false;
    //持续上升的次数
    private int continueUpCount = 0;
    //上一点的持续上升的次数，为了记录波峰的上升次数
    private int continueUpFormerCount = 0;
    //上一点的状态，上升还是下降
    private boolean lastStatus = false;
    //波峰值
    private float peakOfWave = 0;
    //波谷值
    private float valleyOfWave = 0;
    //此次波峰的时间
    private long timeOfThisPeak = 0;
    //上次波峰的时间
    private long timeOfLastPeak = 0;
    //当前的时间
    private long timeOfNow = 0;
    //上次传感器的值
    private float gravityOld = 0;
    //动态阈值需要动态的数据，这个值用于这些动态数据的阈值
    private final float initialValue = (float) 1.7;
    //初始阈值
    private float ThreadValue = (float) 2.0;

    //初始范围
    private float minValue = 11f;
    private float maxValue = 19.6f;

    /**
     * 0-准备计时，1-计时中，2-正常计步中
     */
    private int CountTimeState = 0;
    //记录当前的步数
    public static long CURRENT_STEP = 0;
    //记录临时的步数
    public static long TEMP_STEP = 0;
    //记录上一次临时的步数
    private long lastStep = -1;
    //用x,y,z轴三个维度算出平均值
    public static float average = 0;
    private Timer timer;
    //倒计时3.5秒，3.5秒内不会显示计步，用于屏蔽细微波动
    private long duration = 3500;
    private TimeCount time;
    OnSensorChangeListener onSensorChangeListener;

    // 定义回调函数
    public interface OnSensorChangeListener {
        void onChange();
    }

    //构造函数
    public StepDetector(Context context) {
        super();
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    //监听器set方法
    public void setOnSensorChangeListener(OnSensorChangeListener onSensorChangeListener) {
        this.onSensorChangeListener = onSensorChangeListener;
    }

    //当传感器发生改变后调用的函数
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        //同步块
        synchronized (this) {
            //获取加速度传感器
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                calc_step(event);
            }
        }
    }

    synchronized private void calc_step(SensorEvent event) {
        //算出加速度传感器的x、y、z三轴的平均数值（为了平衡在某一个方向数值过大造成的数据误差）
        average = (float) Math.sqrt(Math.pow(event.values[0], 2)
                + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        detectorNewStep(average);
    }

    private void detectorNewStep(float values) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            if (DetectorPeak(values, gravityOld)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = System.currentTimeMillis();

                if (timeOfNow - timeOfLastPeak >= 200 && (peakOfWave - valleyOfWave >= ThreadValue)
                        && (timeOfNow - timeOfLastPeak) <= 2000) {
                    timeOfThisPeak = timeOfNow;
                    //更新界面的处理，不涉及算法
                    preStep();
                }
                if (timeOfNow - timeOfLastPeak >= 200
                        && (peakOfWave - valleyOfWave >= initialValue)) {
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = Peak_Valley_Thread(peakOfWave - valleyOfWave);
                }
            }
        }
        gravityOld = values;
    }

    private void preStep() {
        if (CountTimeState == 0) {
            time = new TimeCount(duration, 700);
            time.start();
            CountTimeState = 1;  //计时中
            Log.v(TAG, "开启计时器");
        } else if (CountTimeState == 1) {
            TEMP_STEP++;
            Log.v(TAG, "计步中 TEMP_STEP:" + TEMP_STEP);
        } else if (CountTimeState == 2) {
            CURRENT_STEP++;
            Log.v(TAG, "计步回调:" + CURRENT_STEP);
            if (onSensorChangeListener != null) {
                //在这里调用onChange()  因此在StepService中会不断更新状态栏的步数
                onSensorChangeListener.onChange();
            }
        }
    }

    public boolean DetectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }
        if (!isDirectionUp && lastStatus && (continueUpFormerCount >= 2 && (oldValue >= minValue && oldValue < maxValue))) {
            //满足上面波峰的四个条件，此时为波峰状态
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            //满足波谷条件，此时为波谷状态
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    public float Peak_Valley_Thread(float value) {
        float tempThread = ThreadValue;
        if (tempCount < valueNum) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, valueNum);
            for (int i = 1; i < valueNum; i++) {
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[valueNum - 1] = value;
        }
        return tempThread;
    }

    public float averageValue(float[] value, int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / valueNum;  //计算数组均值
        if (ave >= 8) {
            Log.v(TAG, "超过8");
            ave = (float) 4.3;
        } else if (ave >= 7 && ave < 8) {
            Log.v(TAG, "7-8");
            ave = (float) 3.3;
        } else if (ave >= 4 && ave < 7) {
            Log.v(TAG, "4-7");
            ave = (float) 2.3;
        } else if (ave >= 3 && ave < 4) {
            Log.v(TAG, "3-4");
            ave = (float) 2.0;
        } else {
            Log.v(TAG, "else (ave<3)");
            ave = (float) 1.7;
        }
        return ave;
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (lastStep == TEMP_STEP) {
                Log.v(TAG, "onTick 计时停止");
                time.cancel();
                CountTimeState = 0;
                lastStep = -1;
                TEMP_STEP = 0;
            } else {
                lastStep = TEMP_STEP;
            }

        }

        @Override
        public void onFinish() {
            //如果计时器正常结束，则开始计步
            time.cancel();
            CURRENT_STEP += TEMP_STEP;
            lastStep = -1;
            Log.v(TAG, "计时器正常结束");

            timer = new Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    //当步数不在增长的时候停止计步
                    if (lastStep == CURRENT_STEP) {
                        timer.cancel();
                        CountTimeState = 0;
                        lastStep = -1;
                        TEMP_STEP = 0;
                        Log.v(TAG, "停止计步：" + CURRENT_STEP);
                    } else {
                        lastStep = CURRENT_STEP;
                    }
                }
            };
            timer.schedule(task, 0, 2000);   //每隔两秒执行一次，不断监测是否已经停止运动了。
            CountTimeState = 2;
        }
    }
}
