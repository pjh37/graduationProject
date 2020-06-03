package com.example.graduationproject.community.model;

import java.util.ArrayList;

public class ChatRoomDTO {
    private String roomKey;

    private ArrayList<String> userEmails;
    private ArrayList<String> userNicknames;

    private String chatRoomImageUrl;
    private String userCnt;

    private String lastReceiveMessage; // yet
    private String time; // yet

    public String getRoomKey() {
        return roomKey;
    }
    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public ArrayList<String> getUserEmails() {
        return userEmails;
    }
    public void setUserEmails(ArrayList<String> userEmails) {
        this.userEmails = userEmails;
    }

    public String getChatRoomImageUrl() {
        return chatRoomImageUrl;
    }
    public void setChatRoomImageUrl(String chatRoomImageUrl) {this.chatRoomImageUrl = chatRoomImageUrl;}

    public String getUserCnt() {
        return userCnt;
    }
    public void setUserCnt(String userCnt) {
        this.userCnt = userCnt;
    }

    public String getLastReceiveMessage() {
        return lastReceiveMessage;
    }
    public void setLastReceiveMessage(String lastReceiveMessage) {this.lastReceiveMessage = lastReceiveMessage;}

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getUserNicknames() {return userNicknames;}
    public void setUserNicknames(ArrayList<String> userNicknames) {this.userNicknames = userNicknames;}
}
