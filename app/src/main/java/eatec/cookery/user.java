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

    private String shownCookeryRank;
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

    public String convertCookeryRank() {
        if (cookeryRank <= 10) {
            shownCookeryRank = "Level 0: Newcomer";
        }
        else if (cookeryRank <= 20 && cookeryRank > 10) {
            shownCookeryRank = "Level 1: Home Cook";
        }
        else if (cookeryRank <= 30 && cookeryRank > 20) {
            shownCookeryRank = "Level 2: Amateur Chef";
        }
        else if (cookeryRank <= 40 && cookeryRank > 30) {
            shownCookeryRank = "Level 3: Commis Chef";
        }
        else if (cookeryRank <= 50 && cookeryRank > 40) {
            shownCookeryRank = "Level 4: Chef de Partie";
        }
        else if (cookeryRank <= 60 && cookeryRank > 50) {
            shownCookeryRank = "Level 5: Junior Sous Chef";
        }
        else if (cookeryRank <= 70 && cookeryRank > 60) {
            shownCookeryRank = "Level 6: Sous Chef";
        }
        else if (cookeryRank <= 80 && cookeryRank > 70) {
            shownCookeryRank = "Level 7: Head Chef";
        }
        else if (cookeryRank <= 90 && cookeryRank > 80) {
            shownCookeryRank = "Level 8: Executive Chef";
        }
        else if (cookeryRank <= 100 && cookeryRank > 90) {
            shownCookeryRank = "Level 9: Master Chef";
        }
        else if (cookeryRank >= 101) {
            shownCookeryRank = "Cookery God";
        }
        return shownCookeryRank;
    }

}
