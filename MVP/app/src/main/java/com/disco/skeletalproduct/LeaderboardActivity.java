package com.disco.skeletalproduct;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    // UI reference.
    private ImageButton profileButton;
    private ImageButton playButton;
    private ImageButton shopButton;
    private ImageButton leaderBoardButton;
    private ImageButton friendButton;
    private RecyclerView leaderboardView;

    private LeaderboardListAdapter leaderboardAdapter;
    private List<Leaderboard> leaderboardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leaderboard);

        profileButton = (ImageButton) findViewById(R.id.profileImageButton);
        playButton = (ImageButton) findViewById(R.id.playImageButton);
        shopButton = (ImageButton) findViewById(R.id.shopImageButton);
        leaderBoardButton = (ImageButton) findViewById(R.id.leaderboardImageButton);
        friendButton = (ImageButton) findViewById(R.id.friendImageButton);
        leaderboardView = (RecyclerView) findViewById(R.id.leaderboardRecyclerView);

        leaderboardAdapter = new LeaderboardListAdapter(leaderboardList, getApplicationContext(), new ClickListener() {
            @Override public void onPositionClicked(int position) {
                // callback performed on click
            } @Override public void onLongClicked(int position) {
                // callback performed on click
            }});
        LinearLayoutManager layoutManager = new LinearLayoutManager(LeaderboardActivity.this, LinearLayoutManager.VERTICAL, false);
        leaderboardView.setLayoutManager(layoutManager);
        leaderboardView.setAdapter(leaderboardAdapter);
        populateList();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, PlayActivity.class);
                Pair<View, String> pair = Pair.create((View)playButton, "playCircle");
                Pair<View, String> pair2 = Pair.create((View)profileButton, "profileButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(LeaderboardActivity.this, pair2);
                startActivity(intent, options.toBundle());
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
                Pair<View, String> pair = Pair.create((View)profileButton, "profileButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(LeaderboardActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, FriendActivity.class);
                Pair<View, String> pair = Pair.create((View)friendButton, "friendButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(LeaderboardActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, ShopActivity.class);
                Pair<View, String> pair = Pair.create((View)shopButton, "shopButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(LeaderboardActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });
    }


    /**
     * Return the current user's ranking in leaderboard
     * Should pass a user id to it
     */
    public String getRanking() {
        return "1";
    }

    private void populateList(){
//        for (int i = 0; i < 10; i++) {
//            Leaderboard user = new Leaderboard(R.drawable.round_avatar, "Feichi", i+1, (i+1)*10000);
//            leaderboardList.add(user);
//        }
        Leaderboard user0 = new Leaderboard(R.drawable.usericon, "You", 100, 4, false);
        leaderboardList.add(user0);
        Leaderboard user1 = new Leaderboard(R.drawable.usericon, "Feichi", 1, 43734, false);
        leaderboardList.add(user1);
        Leaderboard user2 = new Leaderboard(R.drawable.usericon, "Pengyun", 2, 41753, false);
        leaderboardList.add(user2);
        Leaderboard user3 = new Leaderboard(R.drawable.usericonfemale, "Jialin", 3, 40113, false);
        leaderboardList.add(user3);
        Leaderboard user4 = new Leaderboard(R.drawable.usericonfemale, "Qiyue", 4, 39998,false);
        leaderboardList.add(user4);
        Leaderboard user5 = new Leaderboard(R.drawable.usericonfemale, "Mengmeng", 5, 39887,false);
        leaderboardList.add(user5);
        Leaderboard user6 = new Leaderboard(R.drawable.usericonfemale, "Leiwei", 6, 38886,false);
        leaderboardList.add(user6);
        Leaderboard user7 = new Leaderboard(R.drawable.usericon, "Renzhong", 7, 10000,false);
        leaderboardList.add(user7);
        leaderboardAdapter.notifyDataSetChanged();
    }
}
