package dan.fatzero.mypassword;

public class PwdList {
    private String pwd_name;
    private Integer pwd_id;
    public PwdList(Integer pwd_id,String pwd_name){
        this.pwd_name = pwd_name;
        this.pwd_id = pwd_id;
    }
    public String getPwd_name(){
        return pwd_name;
    }
    public Integer getPwd_id(){
        return pwd_id;
    }
}
