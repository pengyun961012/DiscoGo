package com.disco.skeletalproduct;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

//    private Button goUnityButton;
//    private FloatingActionButton backButton;
    private FloatingActionButton homeButton;
    private List<Song> songList = new ArrayList<>();
    private SongListAdapter songAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);

        viewPager = (ViewPager) findViewById(R.id.songSelectorViewPager);

        songAdapter = new SongListAdapter(PlayActivity.this, songList);
        viewPager.setAdapter(songAdapter);
        populateList();

//        goUnityButton = (Button) findViewById(R.id.go_unity_button);
//        backButton = (FloatingActionButton) findViewById(R.id.backfloatingActionButton);
        homeButton = (FloatingActionButton) findViewById(R.id.homefloatingActionButton);

//        goUnityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100,4096,0);
//                AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, pdh);
//                dispatcher.addAudioProcessor(p);
//
//                new Thread(dispatcher, "Audio Dispatcher").start();
//                Intent intent = new Intent(PlayActivity.this,  UnityPlayerActivity.class);
//                startActivity(intent);
//
//            }
//        });

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateList(){
        Song alphabet = new Song("alphabet", R.drawable.background);
        Song birthday = new Song("birthday", R.drawable.background);
        Song forever = new Song("forever", R.drawable.background);
        songList.add(alphabet);
        songList.add(birthday);
        songList.add(forever);
        songAdapter.notifyDataSetChanged();
    }

}
