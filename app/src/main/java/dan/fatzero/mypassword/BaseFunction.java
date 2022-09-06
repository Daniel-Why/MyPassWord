package dan.fatzero.mypassword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BaseFunction extends AppCompatActivity {
    // 密码哈希
    public  String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }


    //在临时存储中中存入需要跳转的目标页面
    public void share_jump_target(Integer page_id){
        SharedPreferences.Editor editor = GlobalApplication.getAppContext().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("jump_target",page_id);
        editor.apply();
    }


    //读取需要跳转的目标页面
    private Integer get_jump_target(){
        SharedPreferences pref = GlobalApplication.getAppContext().getSharedPreferences("data",MODE_PRIVATE);
        Integer jump_target = pref.getInt("jump_target",0);
        Log.d("JumLog", String.valueOf(jump_target));
        return jump_target;
    }

    // 找到需要跳转的页面并设置intent
    public Intent jump_target_intent(Context context){
        Intent intent;
        Class<?> targetClass = null;
        Integer jump_target = get_jump_target();
        //跳转页配置
        switch (jump_target){
            case 0://默认首页，及生成密码页
                targetClass = MyPwdMainActivity.class;
                break;
            case 1://密码本页面
                targetClass = NotebookActivity.class;
                break;
            default:
        }
        intent = new Intent(GlobalApplication.getAppContext(),targetClass);
        SharedPreferences.Editor editor = GlobalApplication.getAppContext().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("jump_target",0);
        editor.apply();
        Log.d("JumLog","jump_target Change");

        return intent;

    }

}
