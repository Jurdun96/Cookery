package eatec.cookery;

/**
 * Created by Jurdun-PC on 18/02/2020.
 */

class user {

    private String userID;
    private String email;
    private String userName;
    private String password;
    private int cookeryRank;

    public user() {
    }

    public user(String userID, String email, String userName, String password, int cookeryRank) {
        this.userID = userID;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.cookeryRank = cookeryRank;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {return password;}

    public String getUserName() {
        return userName;
    }

    public int getCookeryRank() {
        return cookeryRank;
    }

}
