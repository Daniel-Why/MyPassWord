package dan.fatzero.mypassword;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class NotebookActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        EncryptAndDecrypt encryptAndDecrypt = new EncryptAndDecrypt();
        String encodeString = encryptAndDecrypt.encodeModel01("apple");
        Log.d("DencodeModel","encode:"+encodeString);
        String decodeString = encryptAndDecrypt.decodeModel01(encodeString);
        Log.d("DencodeModel","decode:"+decodeString);










        //设置 toolbar
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.topAppBar_noteBook);
        setSupportActionBar(materialToolbar);
        //设置 toolbar 导航栏返回按钮
        materialToolbar.setNavigationOnClickListener(view -> finish());
    }


}