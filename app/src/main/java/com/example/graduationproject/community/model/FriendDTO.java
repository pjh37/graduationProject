package com.example.graduationproject.community.model;

import android.widget.CheckBox;

import java.io.Serializable;

public class FriendDTO implements Serializable {
    private String userEmail;
    private String profileImageUrl;
    private boolean isSelected; // when ?
    private String userNickname;

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {this.profileImageUrl = profileImageUrl;}

    public String getUserNickname() {return userNickname;}
    public void setUserNickname(String userNickname) {this.userNickname = userNickname;}
}
