package eatec.cookery;

/**
 * Created by Jurdun-PC on 18/02/2020.
 */

public class user {

    String userID;
    String email;
    String userName;
    String cookeryRank;

    private void user() {}

    public user(String userID, String email, String userName, String cookeryRank) {
        this.userID = userID;
        this.email = email;
        this.userName = userName;
        this.cookeryRank = cookeryRank;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getCookeryRank() {
        return cookeryRank;
    }
}
