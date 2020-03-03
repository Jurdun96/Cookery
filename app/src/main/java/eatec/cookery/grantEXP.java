package eatec.cookery;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class grantEXP {
    private Context mContext;
    private String mUID;
    private int mAmount;

    private DatabaseReference mDatabase;

    public grantEXP() {
    }

    public grantEXP(Context context, String UID, int amount) {
        this.mContext = context;
        this.mUID = UID;
        this.mAmount = amount;
    }

    private void addExpToUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(mUID).child("cookeryRank").setValue(mDatabase.child(mUID).child("cookeryRank"));
    }
}
