package com.disco.skeletalproduct;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendActivity extends AppCompatActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    private ImageButton profileButton;
    private ImageButton playButton;
    private ImageButton shopButton;
    private ImageButton leaderBoardButton;
    private ImageButton friendButton;
    private RecyclerView friendView;
    private Button requestButton;
    private SearchView searchFriend;

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
        requestButton = (Button) findViewById(R.id.requestButton);
        searchFriend = (SearchView) findViewById(R.id.friendSearchView);

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
                Pair<View, String> pair2 = Pair.create((View)profileButton, "profileButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(FriendActivity.this, pair2);
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

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
                builder.setTitle("Add or ignore a friend");

                String[] animals = {"horse", "cow", "camel", "sheep", "goat"};
                final int checkedItem = 1; // cow
                builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        checkedItem = which;
                    }
                });

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
//                        String url = "http://165.227.98.119/searchuser/";
//                        sendRequest(url, "llw");
                    }
                });
                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        checkedItem = which;
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        searchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(FriendActivity.this, "search " + searchFriend.getQuery(), Toast.LENGTH_SHORT ).show();
                String username = searchFriend.getQuery().toString();
                Log.d(TAG, "onQueryTextSubmit: " + username);
                String url = "http://165.227.98.119/searchuser/";
                sendRequest(url, username);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

    private void sendRequest(String url, String username) {
        RequestQueue queue = Volley.newRequestQueue(this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", "llw");

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
                finish();
            }
        });

        queue.add(postRequest);
    }
}
