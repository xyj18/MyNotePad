package com.example.mynotepade.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotepade.R;
import com.example.mynotepade.db.DBHelper;

import static com.example.mynotepade.db.DBHelper.PASSWORD;
import static com.example.mynotepade.db.DBHelper.PHONE;
import static com.example.mynotepade.db.DBHelper.USER_TABLE;

public class ChangePsw extends Activity implements View.OnClickListener {

    private EditText edit_oldpsw, edit_newpsw;
    private Button btn_modify, btn_cancel;
    private String oldpassword, newpassword, getuserPhone;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.changepsw_layout);
        init();
    }

    private void init() {
        getuserPhone = getIntent().getStringExtra("UserssPhone");
        edit_oldpsw = (EditText) findViewById(R.id.old_password);
        edit_oldpsw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        edit_newpsw = (EditText) findViewById(R.id.new_password);
        btn_modify = (Button) findViewById(R.id.sure_modify);
        btn_modify.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.cancel_btn);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.sure_modify:
                oldpassword = edit_oldpsw.getText().toString().trim();
                newpassword = edit_newpsw.getText().toString().trim();
                if (oldpassword.equals("") || newpassword.equals("")) {
                    Toast.makeText(ChangePsw.this, "不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (oldpassword.equals(newpassword)) {
                        Toast.makeText(ChangePsw.this, "新旧密码相同,请修正！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkOldPswIsRight(getuserPhone, oldpassword)) {
                            modifyUserPsw(getuserPhone, oldpassword);
                            Toast.makeText(ChangePsw.this, "修改成功,请重新登录！", Toast.LENGTH_SHORT).show();
//                            Intent intenttoLogin = new Intent(ChangePsw.this, LoginActivity.class);
//                            intenttoLogin.putExtra("Phone", getuserPhone);
//                            intenttoLogin.putExtra("Password", newpassword);
//                            setResult(RESULT_OK, intenttoLogin);
//                            startActivity(intenttoLogin);
//                            finish();
                        } else {
                            Toast.makeText(ChangePsw.this, "旧密码不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    private void modifyUserPsw(String getuserPhone, String oldpassword) {
//
//        Log.d("getuserPhone-----", getuserPhone);
//        Log.d("oldpassword-----", oldpassword);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "update " + USER_TABLE + " set " + PASSWORD + "=? where " + PHONE + "=?";
        db.execSQL(sql, new Object[]{getuserPhone, oldpassword});
    }

    private boolean checkOldPswIsRight(String getuserPhone, String oldpassword) {
//
//        Log.d("getuserPhone--in---", getuserPhone);
//        Log.d("oldpassword--in---", oldpassword);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from " + USER_TABLE + " where " + PHONE + "=? and " + PASSWORD + " =?";
        Cursor c = db.rawQuery(sql, new String[]{getuserPhone, oldpassword});
        if (c.getCount() > 0) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }
}
