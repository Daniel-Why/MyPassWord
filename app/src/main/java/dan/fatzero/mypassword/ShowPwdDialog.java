package dan.fatzero.mypassword;

import android.os.Bundle;
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

public class ShowPwdDialog extends DialogFragment {
    public static final String K_TITLE = "k_title";
    public static final String K_CONTENT = "k_content";
    public static final String K_Pwd= "k_pwd";

    private String title;
    private String content;
    private String pwd;
    private View mRootView;
    private Button btn_close;
    private stateListener mStateListener;

    private BaseFunction baseFunction;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle in = getArguments();
        if (in != null) {
            title = in.getString(K_TITLE);
            content = in.getString(K_CONTENT);
            pwd = in.getString(K_Pwd);
        }

        Log.d("SDia","pwd: "+pwd);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            //获取布局
            mRootView = getLayoutInflater().inflate(R.layout.dialog_show_pwd, container, false);
        }

        btn_close = mRootView.findViewById(R.id.dialog_pwd_close_button);
        btn_close.setOnClickListener(view -> mStateListener.close());


        return mRootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleTv = view.findViewById(R.id.dialog_pwd_title);
        TextView contentTv = view.findViewById(R.id.dialog_pwd_content);
        TextView pwdTv = view.findViewById(R.id.dialog_pwd_show);

        Button dialog_pwd_copy_Button = view.findViewById(R.id.dialog_pwd_copy_Button);

        titleTv.setText(title);
        contentTv.setText(content);
        pwdTv.setText(pwd);

        //复制密码
        dialog_pwd_copy_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseFunction.copyToClipboard(pwdTv.getText().toString());
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

}
