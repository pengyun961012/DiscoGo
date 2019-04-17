package com.disco.skeletalproduct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class LeaderboardListAdapter extends RecyclerView.Adapter<LeaderboardListAdapter.ViewHolder> {
    private static String TAG = "DISCO_SKELETAL-----LeaderboardListAdapter";
    private List<Leaderboard> items;
    Context context;
    private final ClickListener listener;

    public LeaderboardListAdapter(List<Leaderboard> items, Context context, ClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Leaderboard item = items.get(position);
        holder.userProfileView.setImageResource(item.getUserProfileImageUrl());
        holder.userNameView.setText(item.getUserName());
        holder.rankView.setText(String.valueOf(item.getRank()));
        holder.scoreView.setText(String.valueOf(item.getScore()));
        holder.isFriendView.setText(String.valueOf(item.isIs_friend()));
        holder.userIdView.setText(String.valueOf(item.getUserId()));
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView userProfileView;
        public TextView userNameView;
        public TextView scoreView;
        public TextView rankView;
        public TextView isFriendView;
        public TextView userIdView;
        public ImageButton addFriendButton;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);

            userProfileView = itemView.findViewById(R.id.userProfileImageView);
            userNameView = itemView.findViewById(R.id.usernameTextView);
            rankView = itemView.findViewById(R.id.rankTextView);
            scoreView = itemView.findViewById(R.id.scoreTextView);
            isFriendView = itemView.findViewById(R.id.leaderIsFriend);
            userIdView = itemView.findViewById(R.id.leaderUserIdView);
            addFriendButton = itemView.findViewById(R.id.followImageButton);

            addFriendButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == addFriendButton.getId()){
                boolean isFriend = Boolean.valueOf(isFriendView.getText().toString());
                String friendName = userNameView.getText().toString();
                if (isFriend){
                    Log.d(TAG, "onClick: add friend: already friend");
                    Toast.makeText(context, friendName + " is already your friend!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "onClick: send request");
                    int index = getAdapterPosition();
                    int friendId = Integer.valueOf(userIdView.getText().toString());
                    String url = context.getResources().getString(R.string.url) + "addpending/";
                    int myId = Integer.valueOf(context.getResources().getString(R.string.my_user_id));
                    sendAddFriendRequest(url, myId, friendId, friendName);
                }
            }
        }
    }

    private void sendAddFriendRequest(String url, int myId, int friendId, final String friendName){
        Log.d(TAG, "sendAddFriendRequest: enter add friend process");
        RequestQueue queue = Volley.newRequestQueue(context);

        HashMap<String, Integer> params = new HashMap<String, Integer>();
        params.put("wantFollower", myId);
        params.put("beFollowed", friendId);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: success add pending request");
                        Toast.makeText(context, "Friend request is sent to " + friendName, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        });

        queue.add(postRequest);
    }
}

