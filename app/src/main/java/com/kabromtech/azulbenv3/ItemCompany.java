package com.kabromtech.azulbenv3;

public class ItemCompany {
    private int mID;
    private String mName;
    private String mDescription;
    private String mPicture;

    public ItemCompany(int ID, String name, String description, String picture){
        mName = name;
        mID = ID;
        mDescription = description;
        mPicture = picture;
    }

    public int getID(){ return mID; }

    public String getName(){
        return mName;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getPicture(){
        return mPicture;
    }
}
