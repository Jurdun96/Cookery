package eatec.cookery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewUserProfile extends AppCompatActivity {
    //Firebase
    private DatabaseReference database;

    private String currentUserUID;
    private String UID;
    private ProgressBar mProgressBar;
    private ImageButton ppImage;
    private TextView bioText;
    private TextView rank;
    private TextView username;
    private TextView followButton;

    private user user;
    private user currentUser;
    private Boolean followed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);

        UID = getIntent().getStringExtra("userID");
        currentUserUID = FirebaseAuth.getInstance().getUid();

        //Init objects
        mProgressBar = findViewById(R.id.rankProgressBar);
        ppImage = findViewById(R.id.ppImageButton);
        bioText = findViewById(R.id.bioText);
        rank = findViewById(R.id.cookeryRankText);
        username = findViewById(R.id.usernameText);
        database = FirebaseDatabase.getInstance().getReference("users");
        followButton = findViewById(R.id.followUserButton);

    }
    @Override
    protected void onStart() {
        super.onStart();
        //Get the user tree data
        Query query = database.orderByChild("userID").equalTo(UID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get user details from database
                user = dataSnapshot.child(UID).getValue(user.class);
                mProgressBar.setProgress(user.getCookeryRank());
                mProgressBar.setMax(100);
                //Set their details in the User details container.
                username.setText(user.getUserName());
                rank.setText(user.convertCookeryRank());

                bioText.setText(user.getBio());

                if (user.getBio().equals("")) {
                    bioText.setText("This user has not added any information about themselves yet!");
                }
                //set Profile Picture
                Picasso.get()
                        .load(user.getProfilePicture())
                        .noPlaceholder()
                        .into(ppImage);
                //TODO biography text
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewUserProfile.this, "There was an error regarding this account", Toast.LENGTH_SHORT).show();
            }
        });

        Query followingRef = database.getRef().child(currentUserUID).child("following");
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Toast.makeText(ViewUserProfile.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                }
                if (dataSnapshot.equals(UID)) {
                    followButton.setText("unfollow user");
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unFollowUser();
                        }
                    });
                }else {
                    followButton.setText("follow user");
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            followUser();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void followUser() {
        database.child(currentUserUID).child("following").child(UID).setValue(user.getUserName());
        Toast.makeText(this, "User followed", Toast.LENGTH_SHORT).show();
    }
    public void unFollowUser(){
        database.child(currentUserUID).child("following").child(UID).removeValue();
        Toast.makeText(this, "user unfollowed", Toast.LENGTH_SHORT).show();
    }
    public void reportUser() {}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
