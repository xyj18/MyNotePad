package com.example.mynotepade.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import com.example.mynotepade.R;
import com.example.mynotepade.db.DBHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends Activity implements View.OnClickListener {

    private String val;
    private Button savebtn, deletebtn;
    private EditText ettext;
    private VideoView v_video;
    private DBHelper dbHelper;
    private SQLiteDatabase dbWriter;
    private File phoneFile, videoFile;
    private String phoneNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        val = getIntent().getStringExtra("flag");
        phoneNum = getIntent().getStringExtra("phoneNumber");
        savebtn = (Button) findViewById(R.id.save);
        deletebtn = (Button) findViewById(R.id.delete);
        ettext = (EditText) findViewById(R.id.ettext);
        v_video = (VideoView) findViewById(R.id.c_video);
        savebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        dbWriter = dbHelper.getWritableDatabase();
        initView();
    }

    private void initView() {
        if (val.equals("1")) { // 文字
            v_video.setVisibility(View.GONE);
        }
        if (val.equals("2")) {
            v_video.setVisibility(View.VISIBLE);
            Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".mp4");
            video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(video, 1);
        }
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addDB();
                finish();
                break;
            case R.id.delete:
                finish();
                break;
        }
    }

    private void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.PHONE, phoneNum);
        cv.put(DBHelper.CONTENT, ettext.getText().toString());
        cv.put(DBHelper.TIME, getTime());
        cv.put(DBHelper.VIDEO, videoFile + "");
        dbWriter.insert(DBHelper.NOTE_TABLE, null, cv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            v_video.setVideoURI(Uri.fromFile(videoFile));
            v_video.start();
        }
    }
}
