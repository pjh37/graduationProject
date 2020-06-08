package com.example.graduationproject.community.model;

public class CommentReplyDTO {
    private Integer _id;

    //    private Integer post_id;
    private Integer comment_id;

    private String target_userEmail;
    private String userEmail;
    private String content;
    private String time;

    private String target_Nickname;
    private String Nickname;

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTarget_userEmail() {return target_userEmail;}
    public void setTarget_userEmail(String target_userEmail) {this.target_userEmail = target_userEmail;}

    public Integer get_id() {
        return _id;
    }
    public void set_id(Integer _id) {
        this._id = _id;
    }

    //    public Integer getPost_id() {
//        return post_id;
//    }
//    public void setPost_id(Integer post_id) {
//        this.post_id = post_id;
//    }
    public Integer getComment_id() {return comment_id;}
    public void setComment_id(Integer comment_id) {this.comment_id = comment_id;}

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

    public String getTarget_Nickname() {return target_Nickname;}
    public void setTarget_Nickname(String target_Nickname) {this.target_Nickname = target_Nickname;}

    public String getNickname() {return Nickname;}
    public void setNickname(String nickname) {Nickname = nickname;}
}
