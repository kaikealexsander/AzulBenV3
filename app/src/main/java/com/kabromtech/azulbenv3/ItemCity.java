package com.kabromtech.azulbenv3;

public class ItemCity {
    private int mID;
    private String mName;
    private String mFkState;

    public ItemCity(int ID, String name, String fkState){
        mName = name;
        mID = ID;
        mFkState = fkState;
    }

    public int getID(){ return mID; }

    public String getName(){
        return mName;
    }

    public String getFkState(){
        return mFkState;
    }
}
