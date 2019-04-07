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
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private ImageButton playButton;
    private ImageButton shopButton;
    private ImageButton leaderBoardButton;
    private ImageButton friendButton;
    private RecyclerView friendView;

    private List<Friend> friendList = new ArrayList<>();
    private FriendListAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend);


        profileButton = (ImageButton) findViewById(R.id.profileImageButton);
        playButton = (ImageButton) findViewById(R.id.playImageButton);
        shopButton = (ImageButton) findViewById(R.id.shopImageButton);
        leaderBoardButton = (ImageButton) findViewById(R.id.leaderboardImageButton);
        friendButton = (ImageButton) findViewById(R.id.friendImageButton);
        friendView = (RecyclerView) findViewById(R.id.friendRecyclerView);

        friendAdapter = new FriendListAdapter(friendList, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(FriendActivity.this, LinearLayoutManager.VERTICAL, false);
        friendView.setLayoutManager(layoutManager);
        friendView.setAdapter(friendAdapter);
        populateList();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, PlayActivity.class);
                Pair<View, String> pair = Pair.create((View)playButton, "playCircle");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(FriendActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, MainActivity.class);
                Pair<View, String> pair = Pair.create((View)profileButton, "profileButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(FriendActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, LeaderboardActivity.class);
                Pair<View, String> pair = Pair.create((View)leaderBoardButton, "leaderButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(FriendActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, ShopActivity.class);
                Pair<View, String> pair = Pair.create((View)shopButton, "shopButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(FriendActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });
    }

    private void populateList(){
//        for (int i = 0; i < 10; i++) {
//            Friend newf = new Friend("Feichi", "Hengheng", "hhh", R.drawable.round_avatar);
//            friendList.add(newf);
//        }
        Friend newf = new Friend("Feichi", "Alphabet", "ABC", R.drawable.usericon);
        friendList.add(newf);
        newf = new Friend("Jialin", "Alphabet", "ABC", R.drawable.usericonfemale);
        friendList.add(newf);
        newf = new Friend("Pengyun", "Alphabet", "ABC", R.drawable.usericon);
        friendList.add(newf);
        friendAdapter.notifyDataSetChanged();
    }
}
