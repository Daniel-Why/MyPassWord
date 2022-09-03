package dan.fatzero.mypassword;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePwdActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private BaseFunction baseFunc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);

        dbHelper = new MyDatabaseHelper(this,"UserPwd.dp",null,1);
        baseFunc = new BaseFunction();

        EditText old_pwd = findViewById(R.id.old_pwd);
        EditText new_pwd = findViewById(R.id.new_pwd);
        EditText confirm_new_pwd = findViewById(R.id.confirm_new_pwd);
        Button set_new_pwd = findViewById(R.id.set_new_pwd);
        TextInputLayout confirm_new_pwd_layout = (TextInputLayout) findViewById(R.id.confirm_new_pwd_layout);

        //设置 toolbar
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.topAppBar_changePwd);
        setSupportActionBar(materialToolbar);
        //设置 toolbar 导航栏返回按钮
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 二次确认密码是否相同
        confirm_new_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String confirm_pwd_text = confirm_new_pwd.getText().toString();
                String new_pwd_text = new_pwd.getText().toString();


                if (confirm_pwd_text.equals(new_pwd_text)){
                    confirm_new_pwd_layout.setErrorEnabled(false);


                }else{
                    confirm_new_pwd_layout.setError("确认密码与新密码不一致");

                }

            }
        });

        set_new_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String new_pwd_text = new_pwd.getText().toString();
                String confirm_pwd_text = confirm_new_pwd.getText().toString();
                if(new_pwd_text.equals("")){
                    Toast.makeText(ChangePwdActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(!confirm_pwd_text.equals(new_pwd_text)){
                    confirm_new_pwd_layout.setError("确认密码与新密码不一致");
                }else{
                    //检测旧密码是否正确
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("UserPwd",null,"id = ?",new String[]{"1"},null,null,null);
                    cursor.moveToFirst();
                    @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                    cursor.close();

                    String old_pwd_text = old_pwd.getText().toString();
                    String hash_pwd_text = baseFunc.getMD5Str(old_pwd_text);

                    if(hash_pwd_text.equals(password) && confirm_pwd_text.equals(new_pwd_text) ){
                        ContentValues values = new ContentValues();
                        values.put("password",baseFunc.getMD5Str(new_pwd_text));
                        db.update("UserPwd",values,"id = ?",new String[]{"1"});
                        Toast.makeText(ChangePwdActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(ChangePwdActivity.this, "旧密码不正确", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });






    }
}