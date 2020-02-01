package com.easysubway.UNIRAIL_service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

public class blocker implements SensorEventListener
{
    static private final float accelation_criteria=1.7f;

    private SensorManager manager;
    private Sensor accelerometer;

    private boolean is_being_accelated=false;

    private Vibrator vibrator;

    public blocker(final Context context)
    {
        manager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public final void block()
    {
        manager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public final void stop()
    {
        manager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(final SensorEvent event)
    {
        final float X_accelation=event.values[0]/ SensorManager.GRAVITY_EARTH;
        final float Y_accelation=event.values[1]/ SensorManager.GRAVITY_EARTH;
        final float Z_accelation=event.values[2]/ SensorManager.GRAVITY_EARTH;

        if(Math.sqrt(X_accelation*X_accelation+Y_accelation*Y_accelation+Z_accelation*Z_accelation)>accelation_criteria)
        {
            if(is_being_accelated==false)
            {
                vibrator.vibrate(250);
            }

            is_being_accelated=true;
        }
        else
        {
            is_being_accelated=false;
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy)
    {
    }
}