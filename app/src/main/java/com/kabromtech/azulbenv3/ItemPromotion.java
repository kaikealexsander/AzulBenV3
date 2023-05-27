package com.kabromtech.azulbenv3;

public class ItemPromotion {
    private int mID;
    private String mName;
    private String mPicture;
    private String mDesc;

    public ItemPromotion(int ID, String name, String picture, String desc){
        mName = name;
        mID = ID;
        mPicture = picture;
        mDesc = desc;
    }

    public int getID(){ return mID; }

    public String getName(){
        return mName;
    }

    public String getPicture(){
        return mPicture;
    }

    public String getDesc(){
        return mDesc;
    }
}