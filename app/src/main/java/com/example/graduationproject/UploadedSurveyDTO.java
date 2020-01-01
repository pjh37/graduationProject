package com.example.graduationproject;

public class UploadedSurveyDTO {
    private String title;
    private int responseCnt;
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResponseCnt() {
        return responseCnt;
    }

    public void setResponseCnt(int responseCnt) {
        this.responseCnt = responseCnt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
