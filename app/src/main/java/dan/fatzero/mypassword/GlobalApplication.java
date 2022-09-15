package dan.fatzero.mypassword;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {
/*
    案例说明
    2022/09/06
    https://segmentfault.com/q/1010000000689855
    解决了普通的类文件（见 BaseFunction）能读取SharedPreferences的问题，既需要在Activity之外拿Context
    在Application中设置一个Context成员，每次使用前通过公有方法获得，然后就应该可以使用getSharedPreferences方法。
 */
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        GlobalApplication.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return GlobalApplication.context;
    }
}