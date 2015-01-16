package com.firerunner.cordova;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class PedometerService extends Service implements SensorEventListener {

    final Timer poller = new Timer();
    TimerTask pollerTask;
    private SensorManager sensorManager;
    private SharedPreferences settings;

    private float todayInitialStepsCount = 0;
    private float todayStepsCount = 0;
    private Date todayStepsDate = null;

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(todayInitialStepsCount == 0 || todayStepsDate.getTime() < today().getTime()) {
            todayInitialStepsCount = event.values[0];
        }

        todayStepsCount = event.values[0] - todayInitialStepsCount;
        todayStepsDate = new Date();

        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("todayStepsCount", todayStepsCount);
        editor.putLong("todayStepsDate", todayStepsDate.getTime());
        editor.putFloat("todayInitialStepsCount", todayInitialStepsCount);
        editor.apply();

        Log.d("Pedometer Service", "Current steps: " + event.values[0]);
        Log.d("Pedometer Service", "Today steps: " + todayStepsCount);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        startPedometer();
        return START_STICKY;
    }

    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPedometer();
    }

    public void startPedometer() {

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        todayInitialStepsCount = settings.getFloat("todayInitialStepsCount", 0);
        todayStepsDate = new Date(settings.getLong("todayStepsDate", (new Date()).getTime()));

        if( todayStepsDate.getTime() < today().getTime()) {
            todayInitialStepsCount = 0;
            todayStepsDate = new Date();

            SharedPreferences.Editor editor = settings.edit();
            editor.putFloat("todayInitialStepsCount", todayInitialStepsCount);
            editor.putLong("todayStepsDate", todayStepsDate.getTime());
            editor.apply();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.d("Pedometer Service", "Step counter sensor not supported...");
            this.stopSelf();
            return;
        }

        final Handler handler = new Handler();
        pollerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Nothing to do here
                        //Log.d("Pedometer Service", "Today steps: " + todayStepsCount);
                    }
                });
            }
        };

        poller.schedule(pollerTask, 0, 30000);
    }

    private void stopPedometer() {
        pollerTask.cancel();
        sensorManager.unregisterListener(this);
    }

    private Date today()
    {
        Long time = new Date().getTime();
        return new Date(time - time % (24 * 60 * 60 * 1000));
    }
}
