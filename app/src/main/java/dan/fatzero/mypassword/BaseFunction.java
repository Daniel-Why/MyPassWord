package dan.fatzero.mypassword;

//import org.apache.commons.codec.binary.Hex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BaseFunction extends AppCompatActivity {
    // 生成随机 UUID
    public String getUUID(){
        String uuid;
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    // 外部存储
    public void saveExternalFile(String str,String file_name,Boolean append) {
        String path = GlobalApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/"+file_name;
        Log.d("SFile","path:"+path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path,append);
            fos.write(str.getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                    Log.d("SFile","外部存储保存成功");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    // 外部获取
    public String getExternalFile(String file_name){
        String str = null;
        FileInputStream fis = null;
        byte[] buffer = null;
        String path = GlobalApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/"+file_name;
        try{
            fis = new FileInputStream(path);
            buffer = new byte[fis.available()];
            fis.read(buffer);

        }catch (IOException e){
            e.printStackTrace();;
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                    str = new String(buffer);
                    Log.d("SFile","外部存储获取成功");
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    //内部存储
    public void saveInternalFile(String str,String file_name,Boolean append) {
        File file = new File(GlobalApplication.getAppContext().getFilesDir(),file_name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file,append);
            fos.write(str.getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                    Log.d("SFile","内部存储保存成功");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //内部获取
    public String getInternalFile(String file_name){
        String str = null;
        FileInputStream fis = null;
        byte[] buffer = null;
        File file = new File(GlobalApplication.getAppContext().getFilesDir(),file_name);
        Log.d("SFile","in_path:"+file);
        try{
            fis = new FileInputStream(file);
            buffer = new byte[fis.available()];
            fis.read(buffer);

        }catch (IOException e){
            e.printStackTrace();;
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                    str = new String(buffer);
                    Log.d("SFile","内部存储获取成功");
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return str;
    }


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




    public static class SymmetricalEncryptionUtils{
        // 生成密钥
        private SecretKeySpec keyGenerate() {
            SecretKeySpec keyBytes;
            StringBuilder RULE = new StringBuilder("123456112345611234561123456112345611234561");
            try {
                if(RULE.length()<16){
                    while (RULE.length()<16) {
                        RULE.append("#");
                    }
                    Log.d("AES", String.valueOf(RULE.length()));

                }else{
                    RULE = new StringBuilder(StringUtils.substring(RULE.toString(), 0, 16));
                }
                byte[] ruleBytes = RULE.toString().getBytes();
                keyBytes = new SecretKeySpec(ruleBytes,"AES");
            } catch (Exception e){
                throw new RuntimeException(e);
            }
            return keyBytes;
        }


        //加密
        public String encodeString (String str){
            byte[] result;
            String encryptString;
            try {
                SecretKeySpec keyBytes = keyGenerate();
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE,keyBytes);
                result = cipher.doFinal(str.getBytes());
                encryptString = android.util.Base64.encodeToString(result,Base64.DEFAULT);
                //encryptString = Base64.getEncoder().encodeToString(result);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
            return encryptString;
        }

        //解密
        public String decodeString (String encryptString){
            String decryptString;
            try{
                SecretKeySpec keyBytes = keyGenerate();
                byte[] encodeByte = android.util.Base64.decode(encryptString,Base64.DEFAULT);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE,keyBytes);
                decryptString = new String(cipher.doFinal(encodeByte));

            }catch (Exception e){
                throw new RuntimeException(e);
            }
            return decryptString;
        }
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
