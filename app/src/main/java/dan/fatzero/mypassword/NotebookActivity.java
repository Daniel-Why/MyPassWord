package dan.fatzero.mypassword;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class NotebookActivity extends AppCompatActivity {

    private List<PwdList> pwdList_List = new ArrayList<>();
    private MyDatabaseHelper dbHelper;
    private PwdListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);


        //设置 toolbar
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar_noteBook);
        setSupportActionBar(materialToolbar);
        //设置 toolbar 导航栏返回按钮
        materialToolbar.setNavigationOnClickListener(view -> finish());


        //pwd list
        initPwdList();
        adapter = new PwdListAdapter(NotebookActivity.this,R.layout.pwd_list_item,pwdList_List);
        ListViewForScrollView mlistView = findViewById(R.id.pwdList_listview);
        mlistView.setAdapter(adapter);

        mlistView.setOnItemClickListener((adapterView, view, i, l) -> {
            //查找对应id的密码原始值
            PwdList pwdList = pwdList_List.get(i);
            Integer pwd_id = pwdList.getPwd_id();
            dbHelper = new MyDatabaseHelper(NotebookActivity.this,"PwdNote.dp",null,1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("PwdNote",null,"id = ?",new String[]{pwd_id.toString()},null,null,null);
            cursor.moveToFirst();
            @SuppressLint("Range") String raw_password = cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();
            //密码解密
            EncryptAndDecrypt encryptAndDecrypt = new EncryptAndDecrypt();
            String password = encryptAndDecrypt.decodeModel01(raw_password);

            //Dialog显示
            popShowPwdDialog(pwdList.getPwd_name(),"显示密码",password);
        });


        //删除列表中的一项
        adapter.setOnItemDeleteClickListener(i -> {
            //查找 id ,并从数据库中删除
            PwdList pwdList = pwdList_List.get(i);
            int pwdId = pwdList.getPwd_id();
            dbHelper = new MyDatabaseHelper(NotebookActivity.this,"PwdNote.dp",null,1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("PwdNote","id = ?",new String[]{String.valueOf(pwdId)});
            //从listview中删除
            pwdList_List.remove(i);
            Toast.makeText(NotebookActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
            //刷新 adapter
            adapter.notifyDataSetChanged();
        });
    }
    // 添加 toolbar menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu_notebook_activity,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_addPwd:
                popAddPwdDialog("添加密码","添加用户自定义设置的密码");
            default:
        }
        return true;

    }

    //初始化数据
    private void initPwdList(){
        dbHelper = new MyDatabaseHelper(this,"PwdNote.dp",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PwdNote",null,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                Log.d("NTB",name);
                Log.d("NTB", String.valueOf(id));
                PwdList thePwd = new PwdList(id,name);
                pwdList_List.add(thePwd);
            }while (cursor.moveToNext());
        }
        cursor.close();


    }

    // Dialog 显示密码
    private void popShowPwdDialog(String title, String content,String pwd) {
        ShowPwdDialog dialog = new ShowPwdDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ShowPwdDialog.K_TITLE, title);
        bundle.putString(ShowPwdDialog.K_CONTENT, content);
        bundle.putString(ShowPwdDialog.K_Pwd, pwd);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "one-tag");
        dialog.setStateListener(dialog::dismiss);
    }

    // Dialog 添加密码
    private void popAddPwdDialog(String title, String content) {
        AddPwdDialog dialog = new AddPwdDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SavePwdDialog.K_TITLE, title);
        bundle.putString(SavePwdDialog.K_CONTENT, content);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "one-tag");
        dialog.setStateListener(dialog::dismiss);
        //刷新listView
        dialog.setmCallback(() -> {
            pwdList_List.clear();
            initPwdList();
            adapter.notifyDataSetChanged();
        });
    }





}