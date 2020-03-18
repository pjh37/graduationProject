package com.example.graduationproject.messageservice;

public class MessageDTO {
    private int _id;
    private String roomKey;
    private String userEmail;
    private String message;
    private String date;
    private int isRead;
    public MessageDTO(int _id,String roomKey,String userEmail,String message,String date,int isRead){
        this._id=_id;
        this.roomKey=roomKey;
        this.userEmail=userEmail;
        this.message=message;
        this.date=date;
        this.isRead=isRead;
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
