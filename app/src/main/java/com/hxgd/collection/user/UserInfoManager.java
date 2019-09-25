package com.hxgd.collection.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hxgd.collection.App;
import com.orhanobut.logger.Logger;

/**
 * 这个东西现在没有啥用了 到时候把它就删了吧。
 */
@Deprecated
public class UserInfoManager {

    private static final String TAG = "UserInfo";

    private static UserInfoManager INSTANCE;

    private volatile UserInfo currentUserInfo;

    private Context mContext;


    private UserInfoManager(){
        mContext =  App.getAppContext();
        try {
            final SharedPreferences sharePe = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            String result = sharePe.getString(TAG,"");
            if (!TextUtils.isEmpty(result)){
                Logger.e("getUserInfoFromSP:" + result);
                UserInfo userInfo = new Gson().fromJson(result,UserInfo.class);
                this.currentUserInfo = userInfo;
            }

        }catch (Exception e){
            Logger.e("getUserInfoFromSP-->Error-:"+e);
        }
        if (currentUserInfo == null){
            createDefalutUserInfo();
        }
//
//        String userInfoJson = SP.get().getString(SP.get().getString(Constant.SP_USER_INFO));
//        Logger.e("获取到的JSon串---->"+userInfoJson);
//        if (!TextUtils.isEmpty(userInfoJson)){
//            UserInfo u = new Gson().fromJson(userInfoJson,UserInfo.class);
//            this.currentUserInfo = u;
//        }else{
//            createDefalutUserInfo();
//        }
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


    public UserInfo getCurrentUserInfo(){
        if (currentUserInfo == null){
            throw new NullPointerException("检查一下用户信息是不是连默认的都没有设置~");
        }
        Logger.e("currentUserInfo---->"+ currentUserInfo.toString());
        return currentUserInfo;
    }


    public synchronized void changeCurrentUserInfo(UserInfo userInfo){
        this.currentUserInfo = userInfo;
        updateUserInfoAndNotify();
    }

    public synchronized void setUserPhone(String userPhone){
        this.currentUserInfo.setUserPhone(userPhone);
        updateUserInfoAndNotify();
    }


    public synchronized void setUserName(String userName){
        this.currentUserInfo.setUserName(userName);
        updateUserInfoAndNotify();
    }

    public synchronized void setUserGender(String userGender){
        this.currentUserInfo.setGender(userGender);
        updateUserInfoAndNotify();
    }

    public synchronized void setUserEducation(String edu){
        this.currentUserInfo.setEducation(edu);
        updateUserInfoAndNotify();

    }

    public synchronized void setBirthday(String birthday){
        this.currentUserInfo.setUserBirthDay(birthday);
        updateUserInfoAndNotify();

    }

    public synchronized void setFatherLauage(String str){
        this.currentUserInfo.setFatherLauages(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setFatherNation(String str){
        this.currentUserInfo.setFatherNation(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setMotherLauage(String str){
        this.currentUserInfo.setMotherLauages(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setMotherNation(String str){
        this.currentUserInfo.setMotherNation(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setMaritalStatus(String str){
        this.currentUserInfo.setMaritalStatus(str);
        updateUserInfoAndNotify();
    }

    public synchronized void setSpouseNation(String str){
        this.currentUserInfo.setSpouseNation(str);
        updateUserInfoAndNotify();
    }


    public void logout(){
        createDefalutUserInfo();
    }


    private void updateUserInfoAndNotify() {
        SharedPreferences sharePre = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String content = new Gson().toJson(currentUserInfo);
        sharePre.edit().putString(TAG, content).commit();
    }

    /**
     * 是否需要补全用户信息
     * @return
     */
    public boolean writeUserInfo(){
        if (!TextUtils.isEmpty(getInstance().getCurrentUserInfo().getUserName())){
            return false;
        }else if (!TextUtils.isEmpty(getInstance().getCurrentUserInfo().getGender())){
            return false;
        }else if (!TextUtils.isEmpty(getInstance().getCurrentUserInfo().getEducation())){
            return false;
        }
        return true;
    }


}
