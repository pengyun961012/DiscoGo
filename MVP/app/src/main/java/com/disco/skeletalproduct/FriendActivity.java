package com.disco.skeletalproduct;

import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

        friendAdapter = new FriendListAdapter(friendList, getApplicationContext(), new ClickListener() {
            @Override public void onPositionClicked(int position) {
                // callback performed on click
            } @Override public void onLongClicked(int position) {
                // callback performed on click
            }});
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
                String url = getResources().getString(R.string.url) + "searchuser/";
                Log.d(TAG, "onQueryTextSubmit: url " + url);
                sendSearchRequest(url, username);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void populateList(){
        Friend newf = new Friend(1,"Feichi", "Alphabet", "ABC", R.drawable.usericon);
        friendList.add(newf);
        newf = new Friend(8,"Jialin", "Alphabet", "ABC", R.drawable.usericonfemale);
        friendList.add(newf);
        newf = new Friend(3,"Pengyun", "Alphabet", "ABC", R.drawable.usericon);
        friendList.add(newf);
        friendAdapter.notifyDataSetChanged();
//        String myId = getResources().getString(R.string.my_user_id);
//        String url = getResources().getString(R.string.url) + "profile/friends/" + myId;
//        getAllFriends(url);
    }

    private void sendSearchRequest(String url, final String username) {
        RequestQueue queue = Volley.newRequestQueue(this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            String searchname = response.getString("username");
                            int imgId = response.getInt("img_id");
                            int tokenNum = response.getInt("token");
                            AlertDialog.Builder dBuilder = new AlertDialog.Builder(FriendActivity.this);

                            LayoutInflater inflater = FriendActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.add_friend_item, null);
                            ImageView userImage = (ImageView) dialogView.findViewById(R.id.addUserProfileImageView);
                            TextView usernameView = (TextView) dialogView.findViewById(R.id.addUsernameTextView);
                            TextView tokenView = (TextView) dialogView.findViewById(R.id.tokenNumTextView);
                            userImage.setImageResource(R.drawable.usericon);
                            usernameView.setText("Username: " + searchname);
                            tokenView.setText("Tokens: " + String.valueOf(tokenNum));

                            dBuilder.setTitle("Search Result");
                            dBuilder.setView(dialogView);
                            dBuilder.setPositiveButton(R.string.send_request, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.d(TAG, "onClick: send add friend request from search");
                                }
                            });
                            dBuilder.setNegativeButton(android.R.string.cancel, null);
                            dBuilder.show();
                        }
                        catch (JSONException e){
                            Log.d(TAG, "onResponse: wrong msg in response" + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
                new AlertDialog.Builder(FriendActivity.this)
                        .setTitle("Search Result")
                        .setMessage("No user with name " + username)
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        queue.add(postRequest);
    }

    private void sendAddFriendRequest(String url, int myId, int friendId){
        Log.d(TAG, "sendAddFriendRequest: enter add friend process");
        RequestQueue queue = Volley.newRequestQueue(FriendActivity.this);

        HashMap<String, Integer> params = new HashMap<String, Integer>();
        params.put("wantFollower", myId);
        params.put("beFollowed", friendId);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: success add pending request");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        });

        queue.add(postRequest);
    }

    private void getAllFriends(String url){
        RequestQueue queue = Volley.newRequestQueue(FriendActivity.this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)  {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray array = response.getJSONArray("friends");
                            for (int i = 0; i < array.length(); i++) {
                                int userid = array.getJSONArray(i).getInt(0);
                                String friendusername = array.getJSONArray(i).getString(1);
                                int imageid = array.getJSONArray(i).getInt(2);
                                String bestsong= array.getJSONArray(i).getString(3);
                                String link = array.getJSONArray(i).getString(4);
                                Log.d(TAG, "onResponse: " + friendusername);
                                Friend newf = new Friend(userid, friendusername, bestsong, link, R.drawable.usericon);
                                friendList.add(newf);
                            }
                            friendAdapter.notifyDataSetChanged();
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
