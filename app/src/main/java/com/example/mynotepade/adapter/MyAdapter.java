package com.example.mynotepade.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mynotepade.R;
import com.example.mynotepade.activity.MainActivity;

import static com.example.mynotepade.db.DBHelper.CONTENT;
import static com.example.mynotepade.db.DBHelper.TIME;
import static com.example.mynotepade.db.DBHelper.VIDEO;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.cell, null);
        TextView contenttv = (TextView) layout.findViewById(R.id.list_content);
        TextView timetv = (TextView) layout.findViewById(R.id.list_time);
        ImageView videoiv = (ImageView) layout.findViewById(R.id.list_video);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex(CONTENT));
        String time = cursor.getString(cursor.getColumnIndex(TIME));
        String urlvideo = cursor.getString(cursor.getColumnIndex(VIDEO));
        contenttv.setText(content);
        Log.d("aaaaaaaa---",content);
        timetv.setText(time);
        videoiv.setImageBitmap(getVideoThumbnail(urlvideo, 80, 80,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        return layout;
    }

    private Bitmap getVideoThumbnail(String urlvideo, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(urlvideo, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
