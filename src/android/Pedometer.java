package com.firerunner.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.*;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pedometer extends CordovaPlugin implements SensorEventListener {

    private SensorManager sensorManager;
    private SharedPreferences settings;

    private float initialStepsCount = 0;
    private Date initialStepsDate = null;

    private float stepsCount = 0;
    private boolean isSupported = true;

    private Date lastRunStart = null;
    private Date lastRunFinish = null;
    private float lastRunStartSteps = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        stepsCount = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        JSONObject returnObj = new JSONObject();

        if(action.equals("isSupported")){

            addProperty(returnObj, "isSupported", isSupported);

        } else if(action.equals("initialize")){

            startService();

            settings = PreferenceManager.getDefaultSharedPreferences(cordova.getActivity());
            initialStepsCount = settings.getFloat("todayStepsCount", 0);
            initialStepsDate = new Date(settings.getLong("todayStepsDate", (new Date()).getTime()));

            sensorManager = (SensorManager) cordova.getActivity().getSystemService(Context.SENSOR_SERVICE);
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                isSupported = false;
            }

        } else if(action.equals("getCurrentReading")){

            addProperty(returnObj, "todayWalkingSteps", (int)(settings.getFloat("todayStepsCount", 0)));
            addProperty(returnObj, "todayWalkingMinutes", 0);
            addProperty(returnObj, "todayWalkingMeters", 0);
            addProperty(returnObj, "todayRunningSteps", 0);
            addProperty(returnObj, "todayRunningMinutes", 0);

            if(lastRunStart != null) {
                Date runFinish = lastRunFinish != null ? lastRunFinish : new Date();
                addProperty(returnObj, "lastRunSteps", (int)(stepsCount - lastRunStartSteps));
                addProperty(returnObj, "lastRunMinutes", (int)((runFinish.getTime()/60000) - (lastRunStart.getTime()/60000)));
            } else {
                addProperty(returnObj, "lastRunSteps", 0);
                addProperty(returnObj, "lastRunMinutes", 0);
            }

        } else if(action.equals("start")){

            lastRunStart = new Date();
            lastRunFinish = null;
            lastRunStartSteps = stepsCount;

            addProperty(returnObj, "isSuccessful", true);

        }  else if(action.equals("stop")){

            lastRunFinish = new Date();
            addProperty(returnObj, "isSuccessful", true);

        } else {

            addProperty(returnObj, "isSuccessful", true);
        }

        callbackContext.success(returnObj);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // don't know if it's ok yet
        //stopService();
    }

    private void startService() {
        Activity context = cordova.getActivity();

        Intent intent = new Intent(
                context, PedometerService.class);
        context.startService(intent);
    }

    private void stopService() {
        Activity context = cordova.getActivity();

        Intent intent = new Intent(
                context, PedometerService.class);
        context.stopService(intent);
    }

    private void addProperty(JSONObject obj, String key, Object value)
    {
        try {
            obj.put(key, value);
        }
        catch (JSONException e) { }

    }

    private Date today()
    {
        Long time = new Date().getTime();
        return new Date(time - time % (24 * 60 * 60 * 1000));
    }
}