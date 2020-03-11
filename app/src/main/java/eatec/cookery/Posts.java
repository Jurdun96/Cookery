package eatec.cookery;

import android.net.Uri;

public class Posts {
    private String mUserID;
    private String mContent;
    private Uri Image;

    public Posts() {
    }

    public Posts(String userID, String content, Uri image) {
        this.mUserID = userID;
        this.mContent = content;
        Image = image;
    }

    public String getmUserID() {
        return mUserID;
    }

    public String getmContent() {
        return mContent;
    }

    public Uri getImage() {
        return Image;
    }
}
