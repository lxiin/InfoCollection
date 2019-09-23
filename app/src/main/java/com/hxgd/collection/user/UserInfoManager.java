package com.hxgd.collection.user;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.hxgd.collection.utils.Constant;
import com.hxgd.collection.utils.SP;
import com.orhanobut.logger.Logger;

public class UserInfoManager {

    private static UserInfoManager INSTANCE;

    private volatile UserInfo userInfo;

    private UserInfoManager(){
        String userInfoJson = SP.get().getString(SP.get().getString(Constant.SP_USER_INFO));
        Logger.e("获取到的JSon串---->"+userInfoJson);
        if (!TextUtils.isEmpty(userInfoJson)){
            UserInfo u = new Gson().fromJson(userInfoJson,UserInfo.class);
            this.userInfo = u;
        }else{
            createDefalutUserInfo();
        }
    }

    private void createDefalutUserInfo() {
        //创建一个空的
        UserInfo userInfo = new UserInfo();
        userInfo.setUserPhone("");
        userInfo.setUserBirthDay("");
        userInfo.setEducation("");
        userInfo.setFatherNation("");
        userInfo.setFatherLauages("");
        userInfo.setGender("");
        userInfo.setMaritalStatus("");
        userInfo.setMotherNation("");
        userInfo.setMotherLauages("");
        userInfo.setSpouseNation("");
        userInfo.setUserName("");
        changeCurrentUserInfo(userInfo);
    }

    public static UserInfoManager getInstance(){
        if (INSTANCE == null){
            synchronized (UserInfoManager.class){
                if (INSTANCE == null){
                    INSTANCE = new UserInfoManager();
                }
            }
        }
        return INSTANCE;
    }


    public UserInfo getUserInfo(){
        if (userInfo == null){
            throw new NullPointerException("检查一下用户信息是不是连默认的都没有设置~");
        }
        Logger.e("userInfo---->"+userInfo.toString());
        return userInfo;
    }


    public synchronized void changeCurrentUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
        updateUserInfoAndNotify();
    }

    public synchronized void setUserPhone(String userPhone){
        this.userInfo.setUserPhone(userPhone);
        updateUserInfoAndNotify();
    }


    public synchronized void setUserName(String userName){
        this.userInfo.setUserName(userName);
        updateUserInfoAndNotify();
    }

    public synchronized void setUserGender(String userGender){
        this.userInfo.setGender(userGender);
        updateUserInfoAndNotify();
    }

    public synchronized void setUserEducation(String edu){
        this.userInfo.setEducation(edu);
        updateUserInfoAndNotify();

    }

    public synchronized void setBirthday(String birthday){
        this.userInfo.setUserBirthDay(birthday);
        updateUserInfoAndNotify();

    }

    public synchronized void setFatherLauage(String str){
        this.userInfo.setFatherLauages(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setFatherNation(String str){
        this.userInfo.setFatherNation(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setMotherLauage(String str){
        this.userInfo.setMotherLauages(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setMotherNation(String str){
        this.userInfo.setMotherNation(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setMaritalStatus(String str){
        this.userInfo.setMaritalStatus(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setSpouseNation(String str){
        this.userInfo.setSpouseNation(str);
        updateUserInfoAndNotify();
    }


    public void logout(){
        createDefalutUserInfo();
    }


    private void updateUserInfoAndNotify() {
        SP.get().putString(Constant.SP_USER_INFO,new Gson().toJson(userInfo));
    }

    public boolean writeUserInfo(){
        if (!TextUtils.isEmpty(getInstance().getUserInfo().getUserName())){
            return false;
        }else if (!TextUtils.isEmpty(getInstance().getUserInfo().getGender())){
            return false;
        }else if (!TextUtils.isEmpty(getInstance().getUserInfo().getEducation())){
            return false;
        }
        return true;
    }


}
