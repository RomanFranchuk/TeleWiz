package com.arcticwolf.telewiz;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class NavigationView extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private String[] data;
    private TypedArray imgs;


    public NavigationView(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            imgs = context.getResources().obtainTypedArray(R.array.menu_icons);
            holder = new ViewHolder();
            holder.textView = (TextView) row.findViewById(R.id.item_id);
            holder.imageView = (ImageView) row.findViewById(R.id.image);
            holder.userImage = (ImageView) row.findViewById(R.id.header_user_image);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),imgs.getResourceId(position,-1));
        String title = data[position];

        holder.imageView.setImageBitmap(bitmap);
        holder.textView.setText(title);

        return row;
    }

    static class ViewHolder{
        ImageView imageView;
        TextView textView;
        ImageView userImage;
    }
}
