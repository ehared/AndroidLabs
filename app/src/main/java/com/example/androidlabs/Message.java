package com.example.androidlabs;

public class Message  {

    private String msg;
    private String msgType;
    public Message (){}

    public Message(String msg, String type){
        this.msg = msg;
        this.msgType = type;
    }
    public String getMsg(){
        return this.msg;
    }
    public String getMessageType() {
        return this.msgType;
    }
}
