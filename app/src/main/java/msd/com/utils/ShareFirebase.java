package msd.com.utils;

/**
 * Pojo class for the share for mapping with firebase
 */
public class ShareFirebase {


    private String name;
    private String Desc;
    private Object shared;

    public Object getShared() {
        return shared;
    }

    public ShareFirebase(){}

    public void setShared(Object shared) {
        this.shared = shared;
    }

    public String getName(){
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getDesc(){
        return Desc;
    }

    public void setDesc(String Desc){
        this.Desc=Desc;
    }

}
