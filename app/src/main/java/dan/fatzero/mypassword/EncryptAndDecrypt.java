package dan.fatzero.mypassword;

public class EncryptAndDecrypt {

    BaseFunction.SymmetricalEncryptionUtils symmetricalEncryptionUtils = new BaseFunction.SymmetricalEncryptionUtils();
    AboutUUID aboutUUID = new AboutUUID();
    BaseFunction baseFunction = new BaseFunction();

    /* 加解密模板 */
    //使用UUID+md5加密
    public String encodeModel01(String str){
        String encryptString;
        String uuid = aboutUUID.getUUID();
        String keyRule = baseFunction.getMD5Str(uuid);
        encryptString = symmetricalEncryptionUtils.encodeString(str,keyRule);
        return encryptString;
    }

    //使用UUID+md5解密
    public String decodeModel01(String str){
        String encryptString;
        String uuid = aboutUUID.getUUID();
        String keyRule = baseFunction.getMD5Str(uuid);
        encryptString = symmetricalEncryptionUtils.decodeString(str,keyRule);
        return encryptString;
    }
}
