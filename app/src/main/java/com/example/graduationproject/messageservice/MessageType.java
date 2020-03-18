package com.example.graduationproject.messageservice;

public enum MessageType {
    SEND(0),RECEIVE(1);
    int value;
    private MessageType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

}
