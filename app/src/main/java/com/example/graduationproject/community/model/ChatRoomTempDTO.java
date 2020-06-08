package com.example.graduationproject.community.model;

public class ChatRoomTempDTO {
    private String roomKey;
    private String userEmail;

    private String userNickname;


    public String getRoomKey() {
        return roomKey;
    }
    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNickname() {return userNickname;}
    public void setUserNickname(String userNickname) {this.userNickname = userNickname;}
}
