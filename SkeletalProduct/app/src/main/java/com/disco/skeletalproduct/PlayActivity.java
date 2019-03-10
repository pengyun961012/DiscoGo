package com.disco.skeletalproduct;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.disco.flappybird.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PlayActivity extends AppCompatActivity {

    private Button goUnityButton;
    private FloatingActionButton backButton;
    private FloatingActionButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);

        goUnityButton = (Button) findViewById(R.id.go_unity_button);
        backButton = (FloatingActionButton) findViewById(R.id.backfloatingActionButton);
        homeButton = (FloatingActionButton) findViewById(R.id.homefloatingActionButton);

        goUnityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100,4096,0);
                AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, pdh);
                dispatcher.addAudioProcessor(p);

                new Thread(dispatcher, "Audio Dispatcher").start();
                Intent intent = new Intent(PlayActivity.this,  UnityPlayerActivity.class);
                startActivity(intent);
                
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    PitchDetectionHandler pdh = new PitchDetectionHandler(){
        @Override
        public void handlePitch(PitchDetectionResult result, AudioEvent e){
            final float pitchInHz = result.getPitch();
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
//                    TextView text = (TextView) findViewById(R.id.textView3);
                    double p  = pitchInHz;
                    if(p<=-1.0){
                        p = 150.0;
                    }
                    UnityPlayer.UnitySendMessage("BirdForeground","receiveData",""+ (int)p);
//                    text.setText(""+p);
                }
            });
        }
    };
}
