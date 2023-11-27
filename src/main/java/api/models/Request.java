package api.models;

public class Request extends BaseModel {

    private boolean approved = false;
    private int id;
    private UserRequest receiver;
    private boolean seen = false;
    private UserRequest sender;
    private String timeStamp;

    public boolean isApproved() {
        return approved;
    }

    public Integer getId() {
        return id;
    }

    public UserRequest getReceiver() {
        return receiver;
    }

    public boolean isSeen() {
        return seen;
    }

    public UserRequest getSender() {
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

    public void setReceiver(UserRequest receiver) {
        this.receiver = receiver;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setSender(UserRequest sender) {
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
