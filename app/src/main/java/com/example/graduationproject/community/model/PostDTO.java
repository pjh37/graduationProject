package com.example.graduationproject.community.model;

import java.util.ArrayList;

public class PostDTO {
    private Integer _id;
    private Integer group_id;
    private String userEmail;
    private String Nickname;
    private String content;
    private String time;


    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer get_id() {
        return _id;
    }
    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getGroup_id() {
        return group_id;
    }
    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {return Nickname;}
    public void setNickname(String Nickname) {this.Nickname = Nickname;}
}
