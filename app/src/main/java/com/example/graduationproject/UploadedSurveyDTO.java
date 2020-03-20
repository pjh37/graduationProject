package com.example.graduationproject;

public class UploadedSurveyDTO {
    private int _id; // db id
    private String title;
    private int response_cnt;
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

    public int getResponse_cnt() {
        return response_cnt;
    }
    public void setResponse_cnt(int response_cnt) {
        this.response_cnt = response_cnt;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
