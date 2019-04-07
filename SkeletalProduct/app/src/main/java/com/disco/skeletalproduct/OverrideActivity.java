package com.disco.skeletalproduct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

public class OverrideActivity extends UnityPlayerActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call UnityPlayerActivity.onCreate()
        super.onCreate(savedInstanceState);
        // print debug message to logcat
        Log.d(TAG, "onCreate called!");
        Intent intent = getIntent();
        fileName = intent.getStringExtra("fileName");
        Log.d(TAG, "onCreate: filename " + fileName);
    }

    @Override
    public void onBackPressed()
    {
        // instead of calling UnityPlayerActivity.onBackPressed() we just ignore the back button event
//        super.onBackPressed();
//        Log.d("OverrideActivity_", "onBackPressed: ");
    }

    public void stopRecorder(){
        SongListAdapter.dispatcher.stop();
        if (SongListAdapter.dispatcher.isStopped()){
            Log.d(TAG, "testMethod: unity function called in override");
        }
    }
}
