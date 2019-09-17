package com.hxgd.collection.bean;

public class BaseEntity {

    public int code;
    public String err;


    public boolean isSuccessful(){
        return code == 200;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", err='" + err + '\'' +
                '}';
    }
}
