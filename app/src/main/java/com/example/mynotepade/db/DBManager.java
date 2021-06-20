package com.example.mynotepade.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mynotepade.entity.UsersInfo;

import java.util.ArrayList;
import java.util.List;

import static com.example.mynotepade.db.DBHelper.USER_TABLE;

public class DBManager {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager() {

    }

    public void add(String phone, String password) {
        db.beginTransaction();//开始事务
        db.execSQL("insert into usertable values(?,?)", new Object[]{
                phone, password
        });
        db.setTransactionSuccessful();//设置事务完成
        db.endTransaction();//结束事务
    }

    public List<UsersInfo> query() {
        ArrayList<UsersInfo> usersInfos = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            UsersInfo usersInfo = new UsersInfo();
            usersInfo.phone = c.getString(c.getColumnIndex("name"));
            usersInfo.password = c.getString(c.getColumnIndex("password"));
            usersInfos.add(usersInfo);
        }
        c.close();
        return usersInfos;
    }

    private Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM " + USER_TABLE, null);
        return c;
    }

    public void closeDB() {
        db.close();
    }
}
