package com.example.mynotepade.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mynotepade.R;
import com.example.mynotepade.db.DBHelper;

public class SelectAct extends Activity implements View.OnClickListener {

    private Button s_delete, s_back;
    private VideoView s_video;
    private TextView s_tv;
    private DBHelper dbHelper;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        init();
    }

    private void init() {
        s_delete = (Button) findViewById(R.id.s_delete);
        s_back = (Button) findViewById(R.id.s_back);
        s_video = (VideoView) findViewById(R.id.s_video);
        s_tv = (TextView) findViewById(R.id.s_tv);
        dbHelper = new DBHelper(this);
        dbWriter = dbHelper.getWritableDatabase();
        s_back.setOnClickListener(this);
        s_delete.setOnClickListener(this);

        if (getIntent().getStringExtra(DBHelper.VIDEO).equals("null")) {
            s_video.setVisibility(View.GONE);
        } else {
            s_video.setVisibility(View.VISIBLE);
        }
        s_tv.setText(getIntent().getStringExtra(DBHelper.CONTENT));
//        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(
//                DBHelper.PATH));
        s_video.setVideoURI(Uri
                .parse(getIntent().getStringExtra(DBHelper.VIDEO)));
        s_video.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s_delete:
                deleteDate();
                finish();
                break;

            case R.id.s_back:
                finish();
                break;
        }
    }

    public void deleteDate() {
        dbWriter.delete(DBHelper.NOTE_TABLE,
                "_id=" + getIntent().getIntExtra(DBHelper.ID, 0), null);
    }
}
