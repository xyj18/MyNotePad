package com.example.mynotepade.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotepade.R;
import com.example.mynotepade.db.DBHelper;

import static com.example.mynotepade.db.DBHelper.PASSWORD;
import static com.example.mynotepade.db.DBHelper.PHONE;
import static com.example.mynotepade.db.DBHelper.USER_TABLE;

public class LoginActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CODE_GO_TO_REGIST = 100;
    private EditText edit_account, edit_password;
    private Button btn_signIn, btn_signUp;
    private String account, password;
    private CheckBox box_rememberpsw;
    private DBHelper dbHelper = new DBHelper(this);
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final long DELAY_TIME = 2000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GO_TO_REGIST:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("Phone");
                    String psw = data.getStringExtra("Password");
                    edit_account.setText(name);
                    edit_password.setText(psw);
                }
                break;
        }
    }

    private void init() {
        edit_account = (EditText) findViewById(R.id.phone_text);
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        edit_password = (EditText) findViewById(R.id.password_text);
        box_rememberpsw = (CheckBox) findViewById(R.id.remember_pass);
        box_rememberpsw = (CheckBox) findViewById(R.id.remember_pass);
        btn_signIn = (Button) findViewById(R.id.signIn_btn);
        btn_signUp = (Button) findViewById(R.id.signUp_btn);
        btn_signIn.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = sharedPreferences.getBoolean("remember_password", false);
        if (isRemember) {
            String name = sharedPreferences.getString("Account", "");
            String psw = sharedPreferences.getString("Password", "");
            edit_account.setText(name);
            edit_password.setText(psw);
            box_rememberpsw.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp_btn:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_GO_TO_REGIST);
                break;
            case R.id.signIn_btn:
                if (edit_account.getText().toString().equals("") ||
                        edit_password.getText().toString().trim().equals("")) {
                    Toast.makeText(LoginActivity.this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();
                }
                break;
        }
    }

    private void readUserInfo() {
        account = edit_account.getText().toString();
        password = edit_password.getText().toString().trim();
        editor = sharedPreferences.edit();
        if (login(account, password)) {
            if (box_rememberpsw.isChecked()) {
                editor.putBoolean("remember_password", true);
                editor.putString("Account", account);
                editor.putString("Password", password);
            } else {
                editor.clear();
            }
            editor.apply();
            Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("UserPhone", account);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "手机号或密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean login(String account, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from " + USER_TABLE + " where " + PHONE + "=? and " + PASSWORD + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{account, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}
