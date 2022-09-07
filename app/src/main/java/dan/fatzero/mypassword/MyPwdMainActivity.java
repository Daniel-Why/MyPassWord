package dan.fatzero.mypassword;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class MyPwdMainActivity extends AppCompatActivity {

private GeneratePassword generatePassword;
private List data_list;



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
        Button save_button = findViewById(R.id.saveButton);
        Chip setPwdCaps = findViewById(R.id.setPwdCaps);
        Chip setPwdSpeChar = findViewById(R.id.setPwdSpeChar);
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(materialToolbar);



        // 调用生成密码类
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

            int alg_id;
            int pwdLen_num;
            int pwdSeed_num;
            int pwdCaps;
            int pwdSpeChar;
            String[] args;

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

            args = new String[]{pwdArg1, pwdArg2};
            alg_id = 1;//算法id,当前只有一个算法，默认取1
            String pwdRaw = generatePassword.main(alg_id,pwdLen_num,pwdSeed_num,pwdCaps,pwdSpeChar,args);
           //String pwdRaw = generatePwd.main(1,pwdLen_num,pwdSeed_num,pwdCaps,pwdSpeChar,args);
            generate_pwd.setText(pwdRaw);
            Toast.makeText(MyPwdMainActivity.this, "密码已生成", Toast.LENGTH_SHORT).show();

            data_list = new ArrayList<Integer>();
            data_list.add(alg_id);
            data_list.add(pwdLen_num);
            data_list.add(pwdSeed_num);
            data_list.add(pwdCaps);
            data_list.add(pwdSpeChar);
            //data_list.add(args);

            Log.d("SPD", "1:"+data_list);
        });

        copy_button.setOnClickListener(view -> {
            Utils.copyToClipboard(this,generate_pwd.getText().toString());
            Toast.makeText(MyPwdMainActivity.this,"复制完成",Toast.LENGTH_SHORT).show();
        });

        save_button.setOnClickListener(view -> {
            Log.d("SPD", "2:" + data_list);
            popSavePwdDialog("保存密码","密码将保存至密码本", (ArrayList<Integer>) data_list);
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

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item){
        //创建临时存储SharedPreferences，并在其中缓存当前页应跳转到认证页后，应该跳转的目标页id


        Intent intent = null;
        boolean caseTag = false;
        switch (item.getItemId()){
            case R.id.menu_changePwd:
                intent  = new Intent(this,ChangePwdActivity.class);
                caseTag = true;
                break;
            case R.id.menu_pwdNote:
                //创建临时存储SharedPreferences，并在其中缓存当前页应跳转到认证页后，应该跳转的目标页id
                BaseFunction.jump2target baseFunc_jump2target = new BaseFunction.jump2target();
                baseFunc_jump2target.share_jump_target(1);//认证后，应跳转至密码本（NootebookActivity）页面
                intent = new Intent(this,LoginActivity.class);
                caseTag = true;
            default:
        }
        if(caseTag){
            startActivity(intent);
        }
        return true;
    }

    // Dialog 保存密码
    private void popSavePwdDialog(String title, String content,ArrayList<Integer>  data_list) {
        SavePwdDialog dialog = new SavePwdDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SavePwdDialog.K_TITLE, title);
        bundle.putString(SavePwdDialog.K_CONTENT, content);
        bundle.putIntegerArrayList("data_list",data_list);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "one-tag");
        dialog.setStateListener(dialog::dismiss);
    }

    }

