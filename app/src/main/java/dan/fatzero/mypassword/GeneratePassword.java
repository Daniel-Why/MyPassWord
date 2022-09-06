package dan.fatzero.mypassword;

import android.util.Log;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class GeneratePassword {
    //生成密码类


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


