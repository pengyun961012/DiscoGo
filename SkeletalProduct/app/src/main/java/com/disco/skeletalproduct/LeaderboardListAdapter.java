package com.disco.skeletalproduct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LeaderboardListAdapter extends RecyclerView.Adapter<LeaderboardListAdapter.ViewHolder> {

    private List<Leaderboard> items;
    Context context;

    public LeaderboardListAdapter(List<Leaderboard> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Leaderboard item = items.get(position);
        holder.userProfileView.setImageResource(item.getUserProfileImageUrl());
        holder.userNameView.setText(item.getUserName());
        holder.rankView.setText(Integer.toString(item.getRank()));
        holder.scoreView.setText(Integer.toString(item.getScore()));
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userProfileView;
        public TextView userNameView;
        public TextView scoreView;
        public TextView rankView;

        public ViewHolder(View itemView) {
            super(itemView);
            userProfileView = itemView.findViewById(R.id.userProfileImageView);
            userNameView = itemView.findViewById(R.id.usernameTextView);
            rankView = itemView.findViewById(R.id.rankTextView);
            scoreView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}

