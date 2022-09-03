package dan.fatzero.mypassword;

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

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MyPwdMainActivity extends AppCompatActivity {



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


        GeneratePwd generatePwd = new GeneratePwd();

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
            String pwdRaw = generatePwd.main(1,pwdLen_num,pwdSeed_num,pwdCaps,pwdSpeChar,args);
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

    //生成密码类
    private class GeneratePwd{


        public String main(int alg_id,int stringLen,int seed,int setCaps,int setSpeChar,String[] args){
            String alg_result = "";
            switch (alg_id){
                case 1://Abby算法
                    alg_result = algAbby(seed,args);
                    break;
                default:
            }

            //截取为一定长度的密码
            String pwdRaw = StringUtils.substring(alg_result,0,stringLen);

            if(setCaps == 1){
                pwdRaw = settingCaps(seed,pwdRaw);
            }

            if(setSpeChar == 1){
                pwdRaw = settingSpeChar(seed,pwdRaw);
            }
            return pwdRaw;
        }

        // 密码算法，算法昵称：Abby，id=1
        public String algAbby (int seed,String[] args){
            String random_string=randomSeed(seed);
            //Toast.makeText(MyPwdMainActivity.this,random_string,Toast.LENGTH_SHORT).show();
            String combineArgs="";
            for(int i = 0;i < args.length;i=i+1){
                combineArgs = combineArgs + args[i];
                Log.d("MyP2","arg:" + args[i]);
            }
            Log.d("MyP2","combineArgs:"+combineArgs);
            String pwdRaw = combineArgs+random_string;
            String md5Str = getMD5Str(pwdRaw);

            return md5Str;
        }


        //String转MD5
        public  String getMD5Str(String str) {
            byte[] digest = null;
            try {
                MessageDigest md5 = MessageDigest.getInstance("md5");
                digest  = md5.digest(str.getBytes("utf-8"));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //16是表示转换为16进制数
            String md5Str = new BigInteger(1, digest).toString(16);
            return md5Str;
        }
        // 随机数
        public String randomSeed(int num){
            Random r = new Random(num);
            String random_string = String.valueOf(r.nextInt(10000));
            return random_string;
        }

        //替换为大写字母
        public String settingCaps(int seed,String rawString){

            String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            char[] c = s.toCharArray();
            for(int i = 0;i<Math.ceil(rawString.length()*0.1);i=i+1){

                Random r = new Random(seed);
                String cap = String.valueOf(c[r.nextInt(c.length)]);
                int rep_index = r.nextInt(rawString.length());
                StringBuilder sb = new StringBuilder(rawString);
                rawString = String.valueOf(sb.replace(rep_index,rep_index+1,cap));
                seed = seed +1;
            }

            String capsString = rawString;
            return capsString;
        }

        public String settingSpeChar(int seed,String rawString){

            String s = "!@#$%^&*_";
            char[] c = s.toCharArray();
            for(int i = 0;i<Math.ceil(rawString.length()*0.1);i=i+1){

                Random r = new Random(seed*2);
                String cap = String.valueOf(c[r.nextInt(c.length)]);
                int rep_index = r.nextInt(rawString.length());
                StringBuilder sb = new StringBuilder(rawString);
                rawString = String.valueOf(sb.replace(rep_index,rep_index+1,cap));
                seed = seed +1;
            }

            String specCharString = rawString;
            return specCharString;
        }

    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = null;
        Boolean caseTag = false;
        switch (item.getItemId()){
            case R.id.menu_changePwd:
                intent  = new Intent(this,ChangePwdActivity.class);
                caseTag = true;
                break;
            case R.id.menu_pwdNote:
                Toast.makeText(this, "尚未开放", Toast.LENGTH_SHORT).show();
                caseTag = false;
            default:
        }
        if(caseTag){
            startActivity(intent);
            caseTag =false;
        }
        return true;
    }

    }

