package com.disco.audioprocess;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private VisualizerView visualizerView;
    private MediaRecorder myAudioRecorder = new MediaRecorder();
    private String outputFile;
    private Button facebookButton;
    private Button googleButton;
    private Button recordButton;
    private Button playButton;

    private Handler handler = new Handler();
    final Runnable updater = new Runnable() {
        public void run() {
            handler.postDelayed(this, 1);
            int maxAmplitude = myAudioRecorder.getMaxAmplitude();
            if (maxAmplitude != 0) {
                visualizerView.addAmplitude(maxAmplitude);
            }
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    10);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);

        }

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        visualizerView = (VisualizerView) findViewById(R.id.visualizer);
        try {
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException | IOException ignored) {

        }
//
//        recordButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    // Pressed
//                    v.performClick();
//                    try {
//                        myAudioRecorder.prepare();
//                        myAudioRecorder.start();
//                    } catch (IllegalStateException ise) {
//                        // make something ...
//                    } catch (IOException ioe) {
//                        // make something
//                    }
//                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
//
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    // Released
//                    try {
//                        myAudioRecorder.stop();
////                        myAudioRecorder.release();
////                        myAudioRecorder = null;
//                        Toast.makeText(getApplicationContext(), "Audio Recorder stopped", Toast.LENGTH_SHORT).show();
//                    } catch (RuntimeException stopException) {
//                        Log.d("recorder", "onTouch: " + stopException.toString());
//                    }
//                }
//                return false;
//            }
//        });
//
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(outputFile);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    // make something
//                }
//            }
//        });

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
        handler.removeCallbacks(updater);
        myAudioRecorder.stop();
        myAudioRecorder.reset();
        myAudioRecorder.release();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        handler.post(updater);
    }

}