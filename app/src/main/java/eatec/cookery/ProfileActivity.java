package eatec.cookery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        getUserDetails();
    }

    public void getUserDetails() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Get the user tree data
        DatabaseReference users = database.getReference("users");
        //get current users UID
        assert currentUser != null;
        UID = currentUser.getUid();
        //TODO edit profile

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView bioText = findViewById(R.id.bioText);
                TextView rank = findViewById(R.id.cookeryRankText);
                TextView username = findViewById(R.id.usernameText);
                //Get the current users Unique ID. Used to find them in the database.
                UID = mAuth.getCurrentUser().getUid();
                //Set their details in the User details container.
                username.setText(dataSnapshot.child(UID).child("userName").getValue().toString());
                rank.setText(dataSnapshot.child(UID).child("cookeryRank").getValue().toString());
                //TODO biography text
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "There was an error regarding your account, contact an administrator.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void signOut(View view) {
        Toast.makeText(this, "You are now signed out", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        finish();
    }
}
