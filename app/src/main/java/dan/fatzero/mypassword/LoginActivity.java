package dan.fatzero.mypassword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private BaseFunction baseFunc;
    private  Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Intent intent = new Intent(LoginActivity.this,MyPwdMainActivity.class);


        dbHelper = new MyDatabaseHelper(this,"UserPwd.dp",null,1);
        baseFunc = new BaseFunction();
        // 实例化对象 jump2target
        BaseFunction.jump2target baseFunc_jump2target = new BaseFunction.jump2target();

        EditText login_pwd = findViewById(R.id.login_pwd);
        Button login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(view -> {
            //隐藏键盘
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("UserPwd",null,"id = ?",new String[]{"1"},null,null,null);
            cursor.moveToFirst();
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();
            String login_pwd_text = login_pwd.getText().toString();
            String hash_pwd_text = baseFunc.getMD5Str(login_pwd_text);
            if(hash_pwd_text.equals(password)){
                Toast.makeText(LoginActivity.this, "认证成功！", Toast.LENGTH_SHORT).show();
                //从临时存储SharedPreferences，中获取此认证页应该跳转的目标页id，并生成intent
                // 从不同页面进入认证后，可以跳转至不同页面
                intent = baseFunc_jump2target.jump_target_intent();
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}