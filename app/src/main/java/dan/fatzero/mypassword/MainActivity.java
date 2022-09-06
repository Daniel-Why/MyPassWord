package dan.fatzero.mypassword;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private BaseFunction baseFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        Button setPwd = (Button)  findViewById(R.id.set_pwd);
        EditText input_pwd = (EditText) findViewById(R.id.input_pwd);
        EditText confirm_pwd = (EditText) findViewById(R.id.confirm_pwd);
        TextInputLayout confirm_pwd_layout = (TextInputLayout) findViewById(R.id.confirm_pwd_layout);

        baseFunc = new BaseFunction();


        baseFunc.share_jump_target(0);//将当前页面位置置为0

        dbHelper = new MyDatabaseHelper(this,"UserPwd.dp",null,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("UserPwd",null,null,null,null,null,null);
        int table_count = cursor.getCount();
        //Log.d("SQL", String.valueOf(table_count));
        cursor.close();

        if(table_count==0){

            confirm_pwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String confirm_pwd_text = confirm_pwd.getText().toString();
                    String input_pwd_text = input_pwd.getText().toString();


                    if (confirm_pwd_text.equals(input_pwd_text)){
                        confirm_pwd_layout.setErrorEnabled(false);

                    }else{
                        confirm_pwd_layout.setError("两次密码输入不一致");
                    }

                }
            });



            setPwd.setOnClickListener(view -> {
                String confirm_pwd_text = confirm_pwd.getText().toString();
                String input_pwd_text = input_pwd.getText().toString();

                //隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //判断密码两次输入是否一致
                if(confirm_pwd_text.equals(input_pwd_text) && !confirm_pwd_text.equals("")){
                    ContentValues values = new ContentValues();
                    String hash_pwd_text = baseFunc.getMD5Str(input_pwd_text);
                    values.put("password",hash_pwd_text);
                    db.insert("UserPwd",null,values);
                    values.clear();
                    Toast.makeText(MainActivity.this, "创建密码成功！", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(MainActivity.this, "创建密码失败！", Toast.LENGTH_SHORT).show();

                }


            });
        }else{
            startActivity(intent);
            finish();
        }
    }


}