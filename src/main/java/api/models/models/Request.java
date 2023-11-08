package api.models.models;

import api.models.basemodel.BaseModel;

public class Request extends BaseModel {

    private boolean approved = false;
    private int id;
    private User receiver;
    private boolean seen = false;
    private User sender;
    private String timeStamp;

    public boolean isApproved() {
        return approved;
    }

    public Integer getId() {
        return id;
    }

    public User getReceiver() {
        return receiver;
    }

    public boolean isSeen() {
        return seen;
    }

    public User getSender() {
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

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setSender(User sender) {
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
