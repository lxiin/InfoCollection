package com.hxgd.collection.db;

import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "record_table")
public class RecordEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "itemName")
    public String itemName;
    @ColumnInfo(name = "filename")
    public String fileName;
    @ColumnInfo(name = "filePath")
    public String filePath;
    @ColumnInfo(name = "recordTime")
    public long recordTime;
    @ColumnInfo(name = "userName")
    public String userName;
    @ColumnInfo(name = "gender")
    public String gender;
    @ColumnInfo(name = "birthday")
    public String birthday;
    @ColumnInfo(name = "education")
    public String education;
    @ColumnInfo(name = "fatherLaguages")
    public String fatherLaguages;
    @ColumnInfo(name = "fatherNation")
    public String fatherNation;
    @ColumnInfo(name = "motherLaguages")
    public String motherLaguages;
    @ColumnInfo(name = "motherNation")
    public String motherNation;
    @ColumnInfo(name = "maritalStatus")
    public String maritalStatus;
    @ColumnInfo(name = "spouseNation")
    public String spouseNation;
    @ColumnInfo(name = "area")
    public String area;
    @ColumnInfo(name = "dialect")
    public String dialect;
    @ColumnInfo(name = "localDialect")
    public String localDialect;
    @ColumnInfo(name = "laguageType")
    public String laguageType;
    @ColumnInfo(name = "population")
    public int population;
    @ColumnInfo(name = "place")
    public String place;
    @ColumnInfo(name = "timeAdd")
    public String timeAdd;
    @ColumnInfo(name = "recordType")
    public int recordType; //1是录音 2是视频
    @ColumnInfo(name = "sync")
    public int sync; //0是没有上传  1是已经上传

    /**
     *
     * @param itemName
     * @param fileName
     * @param filePath
     * @param recordTime
     * @param userName
     * @param gender
     * @param birthday
     * @param education
     * @param fatherLaguages
     * @param fatherNation
     * @param motherLaguages
     * @param motherNation
     * @param maritalStatus
     * @param spouseNation
     * @param area
     * @param dialect
     * @param localDialect
     * @param laguageType
     * @param population
     * @param place
     * @param recordType
     * @param sync
     */
    public RecordEntity(String itemName, String fileName, String filePath, long recordTime,
                        String userName, String gender, String birthday, String education,
                        String fatherLaguages, String fatherNation, String motherLaguages,
                        String motherNation, String maritalStatus, String spouseNation, String area,
                        String dialect, String localDialect, String laguageType, int population,
                        String place,String timeAdd, int recordType, int sync) {
        this.itemName = itemName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.recordTime = recordTime;
        this.userName = userName;
        this.gender = gender;
        this.birthday = birthday;
        this.education = education;
        this.fatherLaguages = fatherLaguages;
        this.fatherNation = fatherNation;
        this.motherLaguages = motherLaguages;
        this.motherNation = motherNation;
        this.maritalStatus = maritalStatus;
        this.spouseNation = spouseNation;
        this.area = area;
        this.dialect = dialect;
        this.localDialect = localDialect;
        this.laguageType = laguageType;
        this.population = population;
        this.place = place;
        this.timeAdd = timeAdd;
        this.recordType = recordType;
        this.sync = sync;
    }


    public boolean isHasSync(){
        return sync == 1;
    }


    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEducation() {
        return education;
    }

    public String getFatherLaguages() {
        return fatherLaguages;
    }

    public String getFatherNation() {
        return fatherNation;
    }

    public String getMotherLaguages() {
        return motherLaguages;
    }

    public String getMotherNation() {
        return motherNation;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getSpouseNation() {
        return spouseNation;
    }

    public String getArea() {
        if (TextUtils.isEmpty(area)){
            return "区域";
        }
        return area;
    }

    public String getDialect() {
        if (TextUtils.isEmpty(dialect)){
            return "方言名称";
        }
        return dialect;
    }

    public String getLocalDialect() {
        if (TextUtils.isEmpty(localDialect)){
            return "土话";
        }
        return localDialect;
    }

    public String getLaguageType() {
        return laguageType;
    }

    public int getPopulation() {
        return population;
    }

    public String getPlace() {
        return place;
    }

    public int getRecordType() {
        return recordType;
    }

    public int getSync() {
        return sync;
    }

    public void setHasSync(){

    }

    public String getTimeAdd() {
        return timeAdd;
    }
}
