package dan.fatzero.mypassword;

import android.util.Log;
import android.widget.Toast;

public class AboutUUID {
    BaseFunction baseFunction = new BaseFunction();
    public String getUUID(){
        String uuid;
        String file_name = "uuid.txt";
        // 查询内部存储
        String internal_uuid = baseFunction.getInternalFile(file_name);
        String external_uuid = baseFunction.getExternalFile(file_name);
        Log.d("SUUID", "internal:"+ internal_uuid);
        Log.d("SUUID","external:"+ external_uuid);
        if (internal_uuid== null && external_uuid == null){//UUID不存在
            uuid = baseFunction.getUUID();
            baseFunction.saveExternalFile(uuid,file_name,false);
            baseFunction.saveInternalFile(uuid,file_name,false);
            Toast.makeText(GlobalApplication.getAppContext(),"UUID 已重新生成",Toast.LENGTH_SHORT).show();
            Log.d("SUUID","UUID不存在,创建 UUID 成功");
        }else if(internal_uuid != null && external_uuid == null){//外部存储中无UUID，内部存储中有
            uuid = baseFunction.getInternalFile(file_name);
            baseFunction.saveExternalFile(uuid,file_name,false);
            Log.d("SUUID","外部存储UUID不存在,已重新存储");
        }else if(internal_uuid == null && external_uuid != null){//外部存储中有UUID，内部存储中无
            uuid = baseFunction.getExternalFile(file_name);
            baseFunction.saveInternalFile(uuid,file_name,false);
            Log.d("SUUID","内部存储UUID不存在,已重新存储");
        }else{
            if(!internal_uuid.equals("")){
                uuid = baseFunction.getInternalFile(file_name);
                baseFunction.saveExternalFile(uuid,file_name,false);
                Log.d("SUUID","uuid 存在");
            }else {
                if (!external_uuid.equals("")){
                    uuid = baseFunction.getExternalFile(file_name);
                    baseFunction.saveInternalFile(uuid,file_name,false);
                    Log.d("SUUID","内部存储UUID不存在,已重新存储");
                }else {
                    uuid = baseFunction.getUUID();
                    baseFunction.saveExternalFile(uuid,file_name,false);
                    baseFunction.saveInternalFile(uuid,file_name,false);
                    Toast.makeText(GlobalApplication.getAppContext(),"UUID 已重新生成",Toast.LENGTH_SHORT).show();
                }
            }

        }

        return uuid;
    }
}
