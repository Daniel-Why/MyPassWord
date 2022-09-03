package dan.fatzero.mypassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

public class ChangePwdActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private BaseFunction baseFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.topAppBar_changePwd);
        setSupportActionBar(materialToolbar);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}