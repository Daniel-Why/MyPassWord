package dan.fatzero.mypassword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PwdListAdapter extends ArrayAdapter<PwdList> {
    private int resourceId;
    private MyDatabaseHelper dbHelper;
    public PwdListAdapter(Context context, int textViewResourceId, List<PwdList> object){
        super(context,textViewResourceId,object);
        resourceId = textViewResourceId;
    }
    public View getView(int position, View coverView, ViewGroup parent){
        PwdList pwdList = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView pwdName = (TextView) view.findViewById(R.id.pwd_name);
        final Button delete_pwd_button = (Button) view.findViewById(R.id.deletePwdButton);
        pwdName.setText(pwdList.getPwd_name());
        delete_pwd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemDeleteListener.onDeleteClick(position);
            }


        });
        return view;

    }
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }


}
