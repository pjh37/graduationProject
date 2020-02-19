package com.example.graduationproject.community.model;

import java.io.Serializable;

public class FriendDTO implements Serializable {
    private String userEmail;
    private String profileImageUrl;
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }


}
