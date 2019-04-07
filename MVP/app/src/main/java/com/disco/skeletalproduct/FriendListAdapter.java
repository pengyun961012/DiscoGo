package com.disco.skeletalproduct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private List<Friend> items;
    Context context;

    public FriendListAdapter(List<Friend> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend item = items.get(position);
        holder.userProfileView.setImageResource(item.getUserImageUrl());
        holder.userNameView.setText(item.getUserName());
        holder.songNameView.setText(item.getSongName());
        holder.albumNameView.setText(item.getAlbumName());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userProfileView;
        public TextView userNameView;
        public TextView songNameView;
        public TextView albumNameView;

        public ViewHolder(View itemView) {
            super(itemView);
            userProfileView = itemView.findViewById(R.id.userProfileImageView);
            userNameView = itemView.findViewById(R.id.usernameTextView);
            songNameView = itemView.findViewById(R.id.songNameTextView);
            albumNameView = itemView.findViewById(R.id.albumNameTextView);
        }
    }
}

