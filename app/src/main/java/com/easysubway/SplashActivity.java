package com.easysubway;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;


public class SplashActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            Thread.sleep(2000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        subway.initialize();                                                                        //+beta 1.2 build 0127
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)                                           //+beta 1.2 build 0127
        {                                                                                           //+beta 1.2 build 0127
            create_channel();                                                                       //+beta 1.2 build 0127
        }                                                                                           //+beta 1.2 build 0127

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private final void create_channel()                                                             //+beta 1.2 build 0127
    {                                                                                               //+beta 1.2 build 0127
        final NotificationManager manager=getSystemService(NotificationManager.class);              //+beta 1.2 build 0127
        manager.createNotificationChannel(new NotificationChannel("important notifications", "important notifications", NotificationManager.IMPORTANCE_HIGH));//+beta 1.2 build 0127
        manager.createNotificationChannel(new NotificationChannel("unimportant notifications", "unimportant notifications", NotificationManager.IMPORTANCE_LOW));//+beta 1.2 build 0127
    }                                                                                               //+beta 1.2 build 0127
}