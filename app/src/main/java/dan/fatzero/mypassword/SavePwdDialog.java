package dan.fatzero.mypassword;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class SavePwdDialog extends DialogFragment {
    public static final String K_TITLE = "k_title";
    public static final String K_CONTENT = "k_content";
    public static final String K_Pwd= "k_pwd";


    private String title;
    private String content;
    private String pwd;
    private View mRootView;
    private Button btn_close;
    private stateListener mStateListener;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle in = getArguments();
        if (in != null) {
            title = in.getString(K_TITLE);
            content = in.getString(K_CONTENT);
            pwd = in.getString(K_Pwd);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            //获取布局
            mRootView = getLayoutInflater().inflate(R.layout.dialog_save_pwd, container, false);
        }

        btn_close = mRootView.findViewById(R.id.dialog_close_button);
        btn_close.setOnClickListener(view -> mStateListener.close());


        return mRootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleTv = view.findViewById(R.id.save_title);
        TextView contentTv = view.findViewById(R.id.save_content);
        TextView saveName = view.findViewById(R.id.save_name);
        Button savedButton = view.findViewById(R.id.dialog_saved_button);
        TextInputLayout saveNameLayout = view.findViewById(R.id.save_name_layout);

        EncryptAndDecrypt encryptAndDecrypt = new EncryptAndDecrypt();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(GlobalApplication.getAppContext(), "PwdNote.dp", null, 1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        titleTv.setText(title);
        contentTv.setText(content);

        //监听密码名称输入框
        saveName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String saveNameText = saveName.getText().toString();
                if(!saveNameText.equals("")){
                    savedButton.setEnabled(true);
                    saveNameLayout.setErrorEnabled(false);

                }else {
                    saveNameLayout.setError("请输入名称！");
                    savedButton.setEnabled(false);

                }
            }
        });

        //按钮监听
        savedButton.setOnClickListener(view1 -> {
            Log.d("SDia","this is dialog");
            String encodeString = encryptAndDecrypt.encodeModel01(pwd);
            ContentValues values = new ContentValues();
            values.put("name",saveName.getText().toString());
            values.put("password",encodeString);
            db.insert("PwdNote",null,values);
            values.clear();
            Toast.makeText(GlobalApplication.getAppContext(),saveName.getText().toString()+" 保存成功！",Toast.LENGTH_SHORT).show();
            mStateListener.close();
        });
    }

    //创建回调，监听dialog的关闭
    public interface stateListener{
        void close();
    }
    //暴露接口
    public void setStateListener(stateListener listener){
        this.mStateListener = listener;
    }

}
