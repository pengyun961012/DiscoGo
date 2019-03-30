package com.disco.skeletalproduct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder> {

    private List<Profile> items;
    Context context;
    private final ClickListener listener;

    public ProfileListAdapter(List<Profile> items, Context context, ClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(v, listener);
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView songNameView;
        public TextView songScoreView;
        public TextView songDurationView;
        public TextView songTimeView;
        public ImageButton songPlayButton;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);

            songNameView = itemView.findViewById(R.id.profileSongNametextView);
            songScoreView = itemView.findViewById(R.id.profileScoretextView);
            songDurationView = itemView.findViewById(R.id.profileDurationtextView);
            songTimeView = itemView.findViewById(R.id.profileTimetextView);
            songPlayButton = itemView.findViewById(R.id.playMusicImageButton);

            songPlayButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == songPlayButton.getId()) {
                Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                
            } else {
                Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }

            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

    }
}
