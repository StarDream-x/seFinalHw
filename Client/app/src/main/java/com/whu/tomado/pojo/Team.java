package com.whu.tomado.pojo;

public class Team {
    private long id;

    private int type;

    private String mems;

    private String desc;

    public long getId() { return id;}
    public int getType() {return type;}
    public String getMems() { return mems;}
    public String getDesc() { return desc;}
    public void setId(long id) { this.id=id;}
    public void setType(int type) { this.type=type;}
    public void setMems(String idList){this.mems=idList;}
    public void setDesc(String desc) { this.desc=desc;}

}
