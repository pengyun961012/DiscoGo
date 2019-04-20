package com.disco.skeletalproduct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.writer.WriterProcessor;

public class OverrideActivity extends UnityPlayerActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    private String fileName;
    private String[] levelList;
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

        levelList = new String[3];
        levelList[0] = "alphabet";
        levelList[1] = "JustTheWayYouAre";
        levelList[2] = "birthday";
    }

    @Override
    public void onBackPressed()
    {
        // instead of calling UnityPlayerActivity.onBackPressed() we just ignore the back button event
//        super.onBackPressed();
//        Log.d("OverrideActivity_", "onBackPressed: ");
    }

    public void stopRecorder(int score, int token){
        if (!SongListAdapter.dispatcher.isStopped()){
            SongListAdapter.dispatcher.stop();
            this.score = score;
            this.token = token;
            changeFileName();
        }
        if (SongListAdapter.dispatcher.isStopped()){
            Log.d(TAG, "testMethod: unity function called in override");
        }

    }

    public void startRecorder(){
        SongListAdapter.dispatcher.run();
//        SongListAdapter.dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100,4096,0);
//        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat( TarsosDSPAudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2*1, 44100, ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder()));
//        File wavfile = new File(getFilesDir(), "temp");
//        Log.e("file created at",getFilesDir().getAbsolutePath());
//        try{
//            RandomAccessFile recordFile = new RandomAccessFile(wavfile, "rw");
//            AudioProcessor p1 = new WriterProcessor(format, recordFile);
//            SongListAdapter.dispatcher.addAudioProcessor(p1);
//        }
//        catch(Throwable e){
//            e.printStackTrace();
//        }
//        AudioProcessor p2 = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, pdh);
//        SongListAdapter.dispatcher.addAudioProcessor(p2);
//
//        new Thread(SongListAdapter.dispatcher, "Audio Dispatcher").start();
    }

    public void levelUp(int level){
        if (level + 1 >= levelList.length){
            return;
        }
        UnityPlayer.UnitySendMessage("GameController","receiveData", levelList[level + 1]);
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
