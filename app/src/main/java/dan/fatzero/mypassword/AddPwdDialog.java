package dan.fatzero.mypassword;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class AddPwdDialog extends DialogFragment {
    public static final String K_TITLE = "k_title";
    public static final String K_CONTENT = "k_content";



    private String title;
    private String content;
    private String pwd;
    private View mRootView;
    private Button btn_close;
    private stateListener mStateListener;

    private MyDatabaseHelper dbHelper;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle in = getArguments();
        if (in != null) {
            title = in.getString(K_TITLE);
            content = in.getString(K_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            //获取布局
            mRootView = getLayoutInflater().inflate(R.layout.dialog_add_pwd, container, false);
        }

        btn_close = mRootView.findViewById(R.id.dialog_add_customized_pwd_close_button);
        btn_close.setOnClickListener(view -> mStateListener.close());


        return mRootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleTv = view.findViewById(R.id.add_customized_pwd_title);
        TextView contentTv = view.findViewById(R.id.add_customized_pwd_content);
        TextView addCustomizedName= view.findViewById(R.id.add_customized_name);
        TextView addCustomizedPwd= view.findViewById(R.id.add_customized_pwd);
        Button savedButton = view.findViewById(R.id.dialog_add_customized_pwd_saved_button);
        TextInputLayout addCustomizedNameLayout = view.findViewById(R.id.add_customized_name_layout);
        TextInputLayout addCustomizedPwdLayout = view.findViewById(R.id.add_customized_pwd_layout);

        EncryptAndDecrypt encryptAndDecrypt = new EncryptAndDecrypt();

        titleTv.setText(title);
        contentTv.setText(content);

        //监听密码名称输入框
        final Boolean[] nameAddedTag = {false};
        final Boolean[] pwdAddedTag = {false};
        addCustomizedName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String saveNameText = addCustomizedName.getText().toString();
                if(!saveNameText.equals("")){
                    nameAddedTag[0] = true;
                    addCustomizedNameLayout.setErrorEnabled(false);
                    if(pwdAddedTag[0] && nameAddedTag[0]){
                        savedButton.setEnabled(true);
                    }else{
                        savedButton.setEnabled(false);
                    }

                }else {
                    nameAddedTag[0] = false;
                    addCustomizedNameLayout.setError("请输入名称！");
                    savedButton.setEnabled(false);

                }
            }
        });


        //监听自定义密码输入框
        addCustomizedPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String saveNameText = addCustomizedPwd.getText().toString();
                if(!saveNameText.equals("")){
                    pwdAddedTag[0] = true;
                    addCustomizedPwdLayout.setErrorEnabled(false);
                    if(pwdAddedTag[0] && nameAddedTag[0]){
                        savedButton.setEnabled(true);
                    }else{
                        savedButton.setEnabled(false);
                    }

                }else {
                    pwdAddedTag[0] = false;
                    addCustomizedPwdLayout.setError("请输入密码！");
                    savedButton.setEnabled(false);

                }
            }
        });

        //按钮监听
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //存储密码
                dbHelper = new MyDatabaseHelper(GlobalApplication.getAppContext(), "PwdNote.dp", null, 1);
                dbHelper.getWritableDatabase();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                pwd = addCustomizedPwd.getText().toString();
                String encodeString = encryptAndDecrypt.encodeModel01(pwd);
                ContentValues values = new ContentValues();
                values.put("name",addCustomizedName.getText().toString());
                values.put("password",encodeString);
                db.insert("PwdNote",null,values);
                values.clear();
                Toast.makeText(GlobalApplication.getAppContext(),addCustomizedName.getText().toString()+" 保存成功！",Toast.LENGTH_SHORT).show();
                // 监听回调,调用刷新ListView
                mCallback.refreshListView();
                mStateListener.close();
            }
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



    // 一个回调,监听
    public interface Callback{
        void refreshListView();
    }

    //    一个接口变量
    private Callback mCallback;

    //一个给接口变量赋值的方法
    public void setmCallback(Callback mCallback){
        this.mCallback=mCallback;
    }
}

