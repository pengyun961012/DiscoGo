package com.disco.skeletalproduct;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();

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
//        Leaderboard user0 = new Leaderboard(R.drawable.usericon, "You", 100, 4, false);
//        leaderboardList.add(user0);
//        Leaderboard user1 = new Leaderboard(R.drawable.usericon, "Feichi", 1, 43734, false);
//        leaderboardList.add(user1);
//        Leaderboard user2 = new Leaderboard(R.drawable.usericon, "Pengyun", 2, 41753, false);
//        leaderboardList.add(user2);
//        Leaderboard user3 = new Leaderboard(R.drawable.usericonfemale, "Jialin", 3, 40113, false);
//        leaderboardList.add(user3);
//        Leaderboard user4 = new Leaderboard(R.drawable.usericonfemale, "Qiyue", 4, 39998,false);
//        leaderboardList.add(user4);
//        Leaderboard user5 = new Leaderboard(R.drawable.usericonfemale, "Mengmeng", 5, 39887,false);
//        leaderboardList.add(user5);
//        Leaderboard user6 = new Leaderboard(R.drawable.usericonfemale, "Leiwei", 6, 38886,false);
//        leaderboardList.add(user6);
//        Leaderboard user7 = new Leaderboard(R.drawable.usericon, "Renzhong", 7, 10000,false);
//        leaderboardList.add(user7);
//        leaderboardAdapter.notifyDataSetChanged();
        String myId = getResources().getString(R.string.my_user_id);
        String url = getResources().getString(R.string.url) + "leaderboard/" + myId;
        getAllLeaderboard(url);
    }

    private void getAllLeaderboard(String url){
        RequestQueue queue = Volley.newRequestQueue(LeaderboardActivity.this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)  {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray array = response.getJSONArray("leaderboard");
                            for (int i = 0; i < array.length(); i++) {
                                int userId = array.getJSONObject(i).getInt("u_id");
                                String leaderUsername = array.getJSONObject(i).getString("username");
                                int imageId = array.getJSONObject(i).getInt("img_id");
                                int tokenNum = array.getJSONObject(i).getInt("token");
                                int rank = array.getJSONObject(i).getInt("rank");
                                boolean is_friend = array.getJSONObject(i).getBoolean("if_friend");
                                Leaderboard user0 = new Leaderboard(userId, R.drawable.usericon, leaderUsername, rank, tokenNum, is_friend);
                                leaderboardList.add(user0);
                            }
                            leaderboardAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse: refresh success");
                        }
                        catch (JSONException e) {
                            Log.d(TAG, "onResponse: refresh error" + e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: error receive request error!");
                    }
                }
        );

        queue.add(getRequest);
    }
}
