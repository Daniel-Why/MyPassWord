package dan.fatzero.mypassword;

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
        return new BigInteger(1, digest).toString(16);
    }


    public static class jump2target{
    /*
    案例说明
    2022/09/06
    https://segmentfault.com/q/1010000000689855
    解决了普通的类文件能读取SharedPreferences的问题，既需要在Activity之外拿Context
    在Application中设置一个Context成员(GlobalApplication)，每次使用前通过公有方法获得，然后就应该可以使用getSharedPreferences方法。
    */
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
        public Intent jump_target_intent(){
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


}
