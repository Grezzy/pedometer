package com.firerunner.Cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.app.Activity;
import android.content.Context;
import android.hardware.*;
import android.os.Bundle;

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
    private float stepsCount = 0;
    private boolean isSupported = true;

    private Date lastRunStart = null;
    private Date lastRunFinish = null;
    private float lastRunStartSteps = 0;


    public Pedometer()
    {
        sensorManager = (SensorManager) cordova.getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            isSupported = false;
        }
    }

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

        if(action == "isSupported"){
            
            addProperty(returnObj, "isSupported", isSupported);

        } else if(action == "getCurrentReading"){

            addProperty(returnObj, "todayWalkingSteps", (int)stepsCount);
            addProperty(returnObj, "todayWalkingMinutes", 0);
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

        } else if(action == "start"){

            lastRunStart = new Date();
            lastRunFinish = null;
            lastRunStartSteps = stepsCount;

            addProperty(returnObj, "isSuccessful", true);

        }  else if(action == "stop"){

            lastRunFinish = new Date();
            addProperty(returnObj, "isSuccessful", true);

        } else {

            addProperty(returnObj, "isSuccessful", true);
        }

        callbackContext.success(returnObj);
        return true;
    }

    private void addProperty(JSONObject obj, String key, Object value)
    {
        try {
            obj.put(key, value);
        }
        catch (JSONException e) { }
    }
}