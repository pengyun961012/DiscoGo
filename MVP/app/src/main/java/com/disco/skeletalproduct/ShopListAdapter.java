package com.disco.skeletalproduct;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ShopListAdapter extends ArrayAdapter<Shop> {
    private List<Shop> items;
    private Activity context;

    public ShopListAdapter(List<Shop> items, Activity context) {
        super(context, R.layout.shop_item, items);
        this.items = items;
        this.context = context;
    }

    static class ViewHolder {
        public TextView itemName;
        public ImageView itemImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.shop_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) view.findViewById(R.id.shopItemNametext);
            viewHolder.itemImage = (ImageView) view.findViewById(R.id.shopItemImage);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Shop item = items.get(position);
        holder.itemImage.setImageResource(item.getImageId());
        holder.itemName.setText(item.getItemName());
        return view;
    }
}
