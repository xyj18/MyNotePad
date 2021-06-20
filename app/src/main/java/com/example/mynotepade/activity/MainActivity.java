package com.example.mynotepade.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotepade.R;
import com.example.mynotepade.adapter.MyAdapter;
import com.example.mynotepade.db.DBHelper;

import static com.example.mynotepade.db.DBHelper.NOTE_TABLE;
import static com.example.mynotepade.db.DBHelper.PHONE;


public class MainActivity extends Activity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView text_phone;
    private ImageView menu;
    private View headerView;
    private Button textbtn, videobtn;
    private ListView lv;
    private Intent intentToAdd;
    private MyAdapter adapter;
    private DBHelper dbHelper;
    private SQLiteDatabase dbReader;
    private Cursor cursor;
    private String stringPhoneForQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        stringPhoneForQuery = getIntent().getStringExtra("UserPhone");

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_na);
        navigationView = (NavigationView) findViewById(R.id.nav);
        menu = (ImageView) findViewById(R.id.main_menu);
        headerView = navigationView.getHeaderView(0);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//关闭手势滑动，只通过点击按钮来滑动
        menu.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);
//                Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(navigationView);
                switch (item.getItemId()) {
                    case R.id.modifypsw:
                        Intent intchangePsw = new Intent(MainActivity.this, ChangePsw.class);
                        intchangePsw.putExtra("UserssPhone", stringPhoneForQuery);
                        startActivity(intchangePsw);
                        break;
                    case R.id.secretDaily:

                        break;
                    case R.id.modifyinfo:

                        break;
                    case R.id.to_login:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.to_out:
                        finish();
                        break;
                }
                return true;
            }
        });
        lv = (ListView) findViewById(R.id.list);
        textbtn = (Button) findViewById(R.id.text);
        videobtn = (Button) findViewById(R.id.video);
        textbtn.setOnClickListener(this);
        videobtn.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        dbReader = dbHelper.getReadableDatabase();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, SelectAct.class);
                i.putExtra(DBHelper.ID, cursor.getInt(cursor.getColumnIndex(DBHelper.ID)));
                i.putExtra(DBHelper.PHONE, cursor.getInt(cursor.getColumnIndex(DBHelper.PHONE)));
                i.putExtra(DBHelper.CONTENT, cursor.getString(cursor.getColumnIndex(DBHelper.CONTENT)));
                i.putExtra(DBHelper.TIME, cursor.getString(cursor.getColumnIndex(DBHelper.TIME)));
                i.putExtra(DBHelper.VIDEO, cursor.getString(cursor.getColumnIndex(DBHelper.VIDEO)));
                startActivity(i);
            }
        });
    }


    @Override
    public void onClick(View v) {
        intentToAdd = new Intent(this, AddContent.class);
        switch (v.getId()) {
            case R.id.main_menu:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                    text_phone = (TextView) findViewById(R.id.text_phone);
                    text_phone.setText(stringPhoneForQuery);
                }
                break;
            case R.id.text:
                intentToAdd.putExtra("flag", "1");
                intentToAdd.putExtra("phoneNumber", stringPhoneForQuery);
                startActivity(intentToAdd);
                break;
            case R.id.video:
                intentToAdd.putExtra("flag", "2");
                intentToAdd.putExtra("phoneNumber", stringPhoneForQuery);
                startActivity(intentToAdd);
                break;
        }
    }

    public void selectDB(String stringPhoneForQuery) {
        String[] args = new String[]{stringPhoneForQuery};
//        cursor=dbReader.rawQuery("SELECT * FROM " + NOTE_TABLE +" where "+PHONE+"=?", new String[]{stringPhoneForQuery});
        cursor = dbReader.query(DBHelper.NOTE_TABLE, null, PHONE + "=?", args, null,
                null, null);
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB(stringPhoneForQuery);
    }
}
