package com.hxgd.collection.event;

import androidx.annotation.Keep;

@Keep
public class RecordEvent {
    private String fileName;
    private String filePath;
    private long time;
    private int type;

    public RecordEvent(String fileName, String filePath, long time, int type) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.time = time;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
