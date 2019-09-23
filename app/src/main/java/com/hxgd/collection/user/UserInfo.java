package com.hxgd.collection.user;

import androidx.annotation.Keep;

/**
 * 这个类不要乱动了 可以添加东西 但是要记得在{@link UserInfoManager}中添加对应的东西
 */
@Keep
public final class UserInfo {

    private String userPhone; //用户手机号  也是唯一标识
    private String userBirthDay; //用户生日
    private String education;  //教育程度
    private String fatherLauages; //父亲语言
    private String fatherNation; //父亲民族
    private String gender; //男
    private String maritalStatus ; //是否结婚
    private String motherLauages ;//母亲语言
    private String motherNation; //母亲民族
    private String spouseNation; //配偶民族
    private String userName;  //用户名

    public UserInfo() {
    }


    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserBirthDay() {
        return userBirthDay;
    }

    public void setUserBirthDay(String userBirthDay) {
        this.userBirthDay = userBirthDay;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getFatherLauages() {
        return fatherLauages;
    }

    public void setFatherLauages(String fatherLauages) {
        this.fatherLauages = fatherLauages;
    }

    public String getFatherNation() {
        return fatherNation;
    }

    public void setFatherNation(String fatherNation) {
        this.fatherNation = fatherNation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMotherLauages() {
        return motherLauages;
    }

    public void setMotherLauages(String motherLauages) {
        this.motherLauages = motherLauages;
    }

    public String getMotherNation() {
        return motherNation;
    }

    public void setMotherNation(String motherNation) {
        this.motherNation = motherNation;
    }

    public String getSpouseNation() {
        return spouseNation;
    }

    public void setSpouseNation(String spouseNation) {
        this.spouseNation = spouseNation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userPhone='" + userPhone + '\'' +
                ", userBirthDay='" + userBirthDay + '\'' +
                ", education='" + education + '\'' +
                ", fatherLauages='" + fatherLauages + '\'' +
                ", fatherNation='" + fatherNation + '\'' +
                ", gender='" + gender + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", motherLauages='" + motherLauages + '\'' +
                ", motherNation='" + motherNation + '\'' +
                ", spouseNation='" + spouseNation + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
