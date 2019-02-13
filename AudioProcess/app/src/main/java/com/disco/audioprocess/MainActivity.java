package com.disco.audioprocess;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private String TAG = "DISCO_AUDIO-----" + this.getClass().getSimpleName();


    private VisualizerView visualizerView;
//    private MediaRecorder myAudioRecorder = new MediaRecorder();
//    private String outputFile;
    private Button facebookButton;
    private Button googleButton;
    private Button recordButton;
    private Button playButton;
    private static TextView frequencyTextview;

    private static int currentFrequency = 0;
    private static int currentVolume = 0;
    private static final int FREQUENCY_CRITICAL = 500;

    public static int SOUND_MESSAGE = 1;
    private SoundAnalysisThread soundAnalysisThread;

    private final Handler handler = new myHandler();
    private static class myHandler extends Handler {
        public void handleMessage(Message msg) {
            // do cool stuff
            switch (msg.what) {
                case 1:
                    Sound sound = (Sound) msg.obj;
                    int frequency = (int) sound.mFrequency;
                    currentVolume = (int) sound.mVolume;
                    frequencyTextview.setText(String.valueOf(frequency));
                    if (frequency <= 0) {
                        return;
                    } else if (Math.abs(frequency - currentFrequency) < FREQUENCY_CRITICAL) {
                        return;
                    } else {
                        currentFrequency = frequency;
                    }
                    break;
            }
        }
    }


    private Handler vishandler = new Handler();
    final Runnable updater = new Runnable() {
        public void run() {
            vishandler.postDelayed(this, 1);
            visualizerView.addAmplitude(currentFrequency);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordButton = (Button) findViewById(R.id.recordButton);
        playButton = (Button) findViewById(R.id.playButton);
        facebookButton = (Button) findViewById(R.id.FacebookLogin);
        googleButton = (Button) findViewById(R.id.GoogleLogin);
        frequencyTextview = (TextView) findViewById(R.id.frequencyTextview);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    10);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);

        }

//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        visualizerView = (VisualizerView) findViewById(R.id.visualizer);
//        try {
//            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            myAudioRecorder.setOutputFile(outputFile);
//            myAudioRecorder.prepare();
//            myAudioRecorder.start();
//        } catch (IllegalStateException | IOException ignored) {
//
//        }
//
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundAnalysisThread = new SoundAnalysisThread(handler);
                soundAnalysisThread.start();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundAnalysisThread != null) {
                    soundAnalysisThread.close();
                }
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FacebookLoginActivity.class);
                startActivity(intent);
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        vishandler.removeCallbacks(updater);
//        myAudioRecorder.stop();
//        myAudioRecorder.reset();
//        myAudioRecorder.release();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        handler.post(updater);
    }

}