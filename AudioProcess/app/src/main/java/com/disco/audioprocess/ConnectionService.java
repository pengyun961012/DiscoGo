package com.disco.audioprocess;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ConnectionService extends Service {
    private int frequency;

    /**
     * receive
     */
    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    frequency = msg.arg1;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger messenger = new Messenger(new ServiceHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    /**
     * send
     */
    private final Handler handler = new Handler();
    private Runnable sendData = new Runnable() {
        // the specific method which will be executed by the handler
        public void run() {
            // sendIntent is the object that will be broadcast outside our app
            Intent sendIntent = new Intent();

            // We add flags for example to work from background
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_FROM_BACKGROUND|Intent.FLAG_INCLUDE_STOPPED_PACKAGES  );

            // SetAction uses a string which is an important name as it identifies the sender of the itent and that we will give to the receiver to know what to listen.
            // By convention, it's suggested to use the current package name
            sendIntent.setAction("com.test.sendintent.IntentToUnity");

            // Here we fill the Intent with our data, here just a string with an incremented number in it.
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Intent ");
            // And here it goes ! our message is send to any other app that want to listen to it.
            sendBroadcast(sendIntent);

            // In our case we run this method each second with postDelayed
            handler.removeCallbacks(this);
            handler.postDelayed(this, 100);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService", "Service Started.");
        handler.removeCallbacks(sendData);
        handler.postDelayed(sendData, 100);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyService", "Service Stopped.");
    }
}