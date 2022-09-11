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

        BaseFunction.SymmetricalEncryptionUtils symEncryptionUtils = new BaseFunction.SymmetricalEncryptionUtils();
        BaseFunction baseFunction = new BaseFunction();

        String encodeString = symEncryptionUtils.encodeString("apple");
        Log.d("AES","加密："+ encodeString);

        String decodeString = symEncryptionUtils.decodeString(encodeString);
        Log.d("AES","解密："+ decodeString);

        String uuid = baseFunction.getUUID();
        Log.d("SFile","UUID:"+uuid);
        baseFunction.saveExternalFile(uuid,"uuid.txt",false);
        String euuid = baseFunction.getExternalFile("uuid.txt");
        Log.d("SFile","EUUID:"+euuid);


        baseFunction.saveInternalFile(uuid,"uuid.txt",false);
        String iuuid = baseFunction.getInternalFile("uuid.txt");
        Log.d("SFile","IUUID:"+iuuid);











        //设置 toolbar
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.topAppBar_noteBook);
        setSupportActionBar(materialToolbar);
        //设置 toolbar 导航栏返回按钮
        materialToolbar.setNavigationOnClickListener(view -> finish());
    }


}