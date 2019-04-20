package com.disco.skeletalproduct;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
//    private Button goUnityButton;
//    private FloatingActionButton backButton;
    private ImageButton profileButton;
    private List<Song> songList = new ArrayList<>();
    private SongListAdapter songAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    10);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    10);

        }

        viewPager = (ViewPager) findViewById(R.id.songSelectorViewPager);

        songAdapter = new SongListAdapter(PlayActivity.this, songList);
        viewPager.setAdapter(songAdapter);
        populateList();


        profileButton = (ImageButton) findViewById(R.id.profileButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                Pair<View, String> pair1 = Pair.create((View)profileButton, "profileButton");
                Pair<View, String> pair2 = Pair.create((View)findViewById(R.id.songImage), "playCircle");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(PlayActivity.this, pair1);
                startActivity(intent, options.toBundle());
            }
        });
    }

    private void populateList(){
        Song alphabet = new Song("alphabet", R.drawable.circlelevel1_arrow);
        Song birthday = new Song("JustTheWayYouAre", R.drawable.circlelevel2_arrow);
        Song forever = new Song("forever", R.drawable.circlelevel3_arrow);
        songList.add(alphabet);
        songList.add(birthday);
        songList.add(forever);
        songAdapter.notifyDataSetChanged();
    }

}
