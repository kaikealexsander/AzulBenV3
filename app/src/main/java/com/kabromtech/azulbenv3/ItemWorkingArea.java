package com.kabromtech.azulbenv3;

public class ItemWorkingArea {
    private int mID;
    private String mName;
    private String mPicture;

    public ItemWorkingArea(int ID, String name, String picture){
        mName = name;
        mID = ID;
        mPicture = picture;
    }

    public int getID(){ return mID; }

    public String getName(){
        return mName;
    }

    public String getPicture(){
        return mPicture;
    }
}