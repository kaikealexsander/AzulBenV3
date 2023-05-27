package com.kabromtech.azulbenv3;

public class ItemSyndicate {
    private int mID;
    private String mName;
    private String mDescription;
    private String mPicture;

    public ItemSyndicate(int ID, String name, String description, String picture){
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
