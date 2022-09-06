package dan.fatzero.mypassword;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {
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