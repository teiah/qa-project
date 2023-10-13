package com.telerikacademy.testframework.models;

public class RequestModel {

    private boolean approved = false;
    private int id;
    private UserModel receiver;
    private boolean seen = false;
    private UserModel sender;
    private String timeStamp;

    public boolean isApproved() {
        return approved;
    }

    public Integer getId() {
        return id;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public boolean isSeen() {
        return seen;
    }

    public UserModel getSender() {
        return sender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Request {\n" +
                "id: " + id + ", \n" +
                "timeStamp: " + timeStamp + ", \n" +
                "approved: " + approved + ", \n" +
                '}';
    }
}
