package com.disco.skeletalproduct;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PendingListAdapter extends ArrayAdapter<PendingFriend> {
    private List<PendingFriend> items;
    private Activity context;

    public PendingListAdapter(List<PendingFriend> items, Activity context) {
        super(context, R.layout.pending_friend_item, items);
        this.items = items;
        this.context = context;
    }

    static class ViewHolder {
        public TextView tokenView;
        public TextView userName;
        public TextView userId;
        public ImageView userImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.pending_friend_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.tokenView = (TextView) view.findViewById(R.id.tokenNumTextView);
            viewHolder.userImage = (ImageView) view.findViewById(R.id.addUserProfileImageView);
            viewHolder.userId = (TextView) view.findViewById(R.id.addUserIdTextView);
            viewHolder.userName = (TextView) view.findViewById(R.id.addUsernameTextView);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.userName.setText(items.get(position).getUserName());
        holder.userId.setText(String.valueOf(items.get(position).getUserId()));
        holder.tokenView.setText("token:" + String.valueOf(items.get(position).getTokenNum()));
        holder.userImage.setImageResource(R.drawable.usericon);
        return view;
    }
}
