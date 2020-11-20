package com.example.androidlabs;

public class Message {

    private String msg;
    private boolean isSent;
    private long id;

    public Message() {
    }

    public Message(String msg, boolean msgType, long id) {
        this.msg = msg;
        this.isSent = msgType;
        this.id = id;
    }

    public String getMsg() {
        return this.msg;
    }

    public boolean getMessageType() {
        return this.isSent;
    }

    public long getId() {
        return id;
    }
}
