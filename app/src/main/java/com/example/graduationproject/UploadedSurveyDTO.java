package com.example.graduationproject;

public class UploadedSurveyDTO {
    private int _id;
    private String title;
    private int responseCnt;
    private String time;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

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
