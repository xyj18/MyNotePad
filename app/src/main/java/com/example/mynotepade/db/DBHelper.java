package com.example.mynotepade.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bonbo on 2018/6/19.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Date.db";
    public static final String USER_TABLE = "usersinfo";
    public static final String PHONE = "userphone";
    public static final String PASSWORD = "password";

    public static final String NOTE_TABLE = "notesinfo";
    public static final String CONTENT = "content";
    //    public static final String PATH = "path";
    public static final String VIDEO = "video";
    public static final String ID = "_id";
    public static final String TIME = "time";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //新建用户信息表
        db.execSQL("CREATE TABLE " + USER_TABLE + " ("
                + PHONE + " TEXT PRIMARY KEY, "
                + PASSWORD + " TEXT) ");
        //新建用户记事表
        db.execSQL("CREATE TABLE " + NOTE_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PHONE + " TEXT, "
                + CONTENT + " TEXT, "
                + VIDEO + " TEXT, "
                + TIME + " TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table " + USER_TABLE + " add column other string");
        db.execSQL("alter table " + NOTE_TABLE + " add column other string");
    }
}
