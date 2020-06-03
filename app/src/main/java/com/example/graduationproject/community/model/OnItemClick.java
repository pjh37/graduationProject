package com.example.graduationproject.community.model;

public interface OnItemClick {
    void onPostObjectClick(int post_id, int type);
    void setTargetUserEmail(String target);
    void setTargetNickname(String target);
    void onCommentDelClick(int _id, int type);
}
