package com.disco.skeletalproduct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

import java.io.File;

public class OverrideActivity extends UnityPlayerActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    private String fileName;
    private int score;
    private int token;

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

    public void stopRecorder(int score, int token){
        SongListAdapter.dispatcher.stop();
        if (SongListAdapter.dispatcher.isStopped()){
            Log.d(TAG, "testMethod: unity function called in override");
        }
        this.score = score;
        this.token = token;
        changeFileName();
    }

    private void changeFileName(){
        String currentFileName = "temp";
        Log.i("Current file name", currentFileName);

        File directory = getFilesDir();
        File from      = new File(directory, currentFileName);
        File to        = new File(directory, fileName + "_" + Integer.toString(score));
        from.renameTo(to);
    }
}
