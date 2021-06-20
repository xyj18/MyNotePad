package com.example.mynotepade.activity;

import android.app.Activity;
import android.content.ContentValues;
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
import com.example.mynotepade.db.DBManager;

import static com.example.mynotepade.db.DBHelper.PASSWORD;
import static com.example.mynotepade.db.DBHelper.PHONE;
import static com.example.mynotepade.db.DBHelper.USER_TABLE;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private static final int MAX_SIZE = 11;
    private EditText edit_setPhone, edit_setPassword, edit_resetPassword;
    private Button btn_confirm, btn_cancel;
    private DBHelper dbHelper;
    private String phone, password, passwordAgain;
    private DBManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_layout);
        init();
    }

    private void init() {
        dbHelper = new DBHelper(this);
        edit_setPhone = (EditText) findViewById(R.id.phone_text);
        edit_setPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        edit_setPassword = (EditText) findViewById(R.id.password_text);
        edit_resetPassword = (EditText) findViewById(R.id.passwordagain_text);
        btn_confirm = (Button) findViewById(R.id.signUp_btn);
        btn_confirm.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.cancel_btn);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.signUp_btn:
                phone = edit_setPhone.getText().toString();
                password = edit_setPassword.getText().toString().trim();
                passwordAgain = edit_resetPassword.getText().toString().trim();

                Log.d("phone+",phone);
                Log.d("phone++",password);
                Log.d("phone++",passwordAgain);

                if (phone.equals("") || password.equals("") || passwordAgain.equals("")) {
                    Toast.makeText(RegisterActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (phone.length() != MAX_SIZE) {
                        Toast.makeText(RegisterActivity.this, "非法的手机号,请重新输入！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (CheckIsDataAlreadyInDBOrNot(phone)) {
                            Toast.makeText(RegisterActivity.this, "该手机号已被注册，注册失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (password.equals(passwordAgain)) {
                                RegisterUserInfo(phone, password);
                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                Intent intent_1 = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent_1.putExtra("Phone", phone);
                                intent_1.putExtra("Password", password);
                                setResult(RESULT_OK, intent_1);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void RegisterUserInfo(String phone, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHONE, phone);
        values.put(PASSWORD, password);
        db.insert(USER_TABLE, null, values);
        db.close();
    }

    private boolean CheckIsDataAlreadyInDBOrNot(String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from "+USER_TABLE+" where "+PHONE+"=?";
        Cursor c = db.rawQuery(sql, new String[]{phone});
        if (c.getCount() > 0) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }
}
