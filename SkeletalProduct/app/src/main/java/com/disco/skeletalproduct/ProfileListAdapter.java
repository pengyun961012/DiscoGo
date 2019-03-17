package com.disco.skeletalproduct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder> {

    private List<Profile> items;
    Context context;

    public ProfileListAdapter(List<Profile> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Profile item = items.get(position);
        holder.songNameView.setText(item.getSongName());
        holder.songScoreView.setText(Integer.toString(item.getSongScore()));
        String duration = Integer.toString(item.getSongDurationMinute()) + " : " + Integer.toString(item.getSongDurationSecond());
        holder.songDurationView.setText(duration);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.songTimeView.setText(dateFormat.format(item.getSongTime()));
        holder.itemView.setTag(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songNameView;
        public TextView songScoreView;
        public TextView songDurationView;
        public TextView songTimeView;

        public ViewHolder(View itemView) {
            super(itemView);
            songNameView = itemView.findViewById(R.id.profileSongNametextView);
            songScoreView = itemView.findViewById(R.id.profileScoretextView);
            songDurationView = itemView.findViewById(R.id.profileDurationtextView);
            songTimeView = itemView.findViewById(R.id.profileTimetextView);
        }
    }
}
