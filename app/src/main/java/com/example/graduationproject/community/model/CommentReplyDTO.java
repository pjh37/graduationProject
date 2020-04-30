package com.example.graduationproject.community.model;

public class CommentReplyDTO {
    private Integer _id;
    private Integer post_id;
    private String target_userEmail;
    private String userEmail;
    private String content;
    private String time;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTargetUserEmail() { return target_userEmail;}

    public void setTargetUserEmail(String target_userEmail) { this.target_userEmail = target_userEmail; }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
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
}
