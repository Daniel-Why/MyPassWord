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

import java.util.ArrayList;

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
        };

        Log.d("SPD", title);
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
        Button button = view.findViewById(R.id.dialog_saved_button);

        titleTv.setText(title);
        contentTv.setText(content);
        button.setOnClickListener(view1 -> {
            Log.d("SDia","this is dialog");
            Toast.makeText(GlobalApplication.getAppContext(),"this is dialog",Toast.LENGTH_SHORT).show();
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
