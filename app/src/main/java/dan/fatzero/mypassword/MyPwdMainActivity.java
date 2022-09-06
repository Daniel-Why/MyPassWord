package dan.fatzero.mypassword;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;

public class MyPwdMainActivity extends AppCompatActivity {

private GeneratePassword generatePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pwd_main);

        EditText setPwsArg1 = findViewById(R.id.setPwsArg1);
        EditText setPwsArg2 = findViewById(R.id.setPwsArg2);
        EditText setPwsSeed = findViewById(R.id.setPwsSeed);
        EditText setPwsLen =  findViewById(R.id.setPwdLen);
        Button generate_button=  findViewById(R.id.generate_button);
        TextView generate_pwd = findViewById(R.id.generate_pwd);
        Button copy_button = findViewById(R.id.copyButton);
        Chip setPwdCaps = findViewById(R.id.setPwdCaps);
        Chip setPwdSpeChar = findViewById(R.id.setPwdSpeChar);
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(materialToolbar);


        generatePassword = new GeneratePassword();

        //生成密码
        generate_button.setOnClickListener(view -> {
            //隐藏键盘
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


            //根据参数计算密码
            String pwdArg1 = setPwsArg1.getText().toString();
            String pwdArg2 = setPwsArg2.getText().toString();
            String pwdSeed = setPwsSeed.getText().toString();
            String pwdLen= setPwsLen.getText().toString();
            int pwdSeed_num ;
            int pwdLen_num ;
            int pwdCaps;
            int pwdSpeChar;

            // ”大写字母“ 复选框是否开启
            if(setPwdCaps.isChecked()){
                pwdCaps = 1;
            }else {
                pwdCaps = 0;
            }

            //”特殊符号“ 复选框是否开启
            if(setPwdSpeChar.isChecked()){
                pwdSpeChar = 1;
            }else {
                pwdSpeChar = 0;
            }

            // 判断 Seed 是否为空，若为空则取用默认值 1
            if(!pwdSeed.equals("")){
                pwdSeed_num = Integer.parseInt(pwdSeed);
            }else {
                pwdSeed_num = 1;
            }

            // 判断 长度 是否为空，若为空则取用默认值 8
            if(!pwdLen.equals("")){
                pwdLen_num = Integer.parseInt(pwdLen);
            }else {
                pwdLen_num = 8;
            }

            String[] args = {pwdArg1,pwdArg2};
            String pwdRaw = generatePassword.main(1,pwdLen_num,pwdSeed_num,pwdCaps,pwdSpeChar,args);
           //String pwdRaw = generatePwd.main(1,pwdLen_num,pwdSeed_num,pwdCaps,pwdSpeChar,args);
            generate_pwd.setText(pwdRaw);
            Toast.makeText(MyPwdMainActivity.this, "密码已生成", Toast.LENGTH_SHORT).show();
        });

        copy_button.setOnClickListener(view -> {
            Utils.copyToClipboard(this,generate_pwd.getText().toString());
            Toast.makeText(MyPwdMainActivity.this,"复制完成",Toast.LENGTH_SHORT).show();
        });



    }





    //复制至剪贴板
    public static class Utils {

        // 将文本复制到剪切板
        public static void copyToClipboard(Context context, String content) {
            // 从 API11 开始 android 推荐使用 android.content.ClipboardManager
            // 为了兼容低版本我们这里使用旧版的 android.text.ClipboardManager，虽然提示 deprecated，但不影响使用。
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(content);
            Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
        }

    }



    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        BaseFunction baseFunc = new BaseFunction();
        Intent intent = null;
        Boolean caseTag = false;
        switch (item.getItemId()){
            case R.id.menu_changePwd:
                intent  = new Intent(this,ChangePwdActivity.class);
                caseTag = true;
                break;
            case R.id.menu_pwdNote:
                baseFunc.share_jump_target(1);
                intent = new Intent(this,LoginActivity.class);
                caseTag = true;
            default:
        }
        if(caseTag){
            startActivity(intent);
            caseTag =false;
        }
        return true;
    }

    }

