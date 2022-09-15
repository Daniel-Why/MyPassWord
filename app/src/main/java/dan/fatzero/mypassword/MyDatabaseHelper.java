package dan.fatzero.mypassword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    //创建账户密码数据库
    public static final String CREATE_UserPwd = "Create table UserPwd("
            +"id integer primary key autoincrement,"
            +"password text)";
    //创建密码本数据库
    public static final String CREATE_PwdNote = "Create table PwdNote("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"password text)";

    private final Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_UserPwd);
        db.execSQL(CREATE_PwdNote);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        db.execSQL("drop table if exists UserPwd");
        onCreate(db);
    }


}