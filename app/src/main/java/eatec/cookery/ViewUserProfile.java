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

import java.util.ArrayList;
import java.util.List;

public class ViewUserProfile extends AppCompatActivity {
    //Firebase
    private DatabaseReference usersDatabase;
    private DatabaseReference followingDatabase;

    private String currentUserUID;
    private String UID;
    private ProgressBar mProgressBar;
    private ImageButton ppImage;
    private TextView bioText;
    private TextView rank;
    private TextView username;

    private TextView followButton;
    private TextView unfollowButton;

    private user user;
    private List<String> usersList;

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

        usersDatabase = FirebaseDatabase.getInstance().getReference("users");
        followingDatabase = FirebaseDatabase.getInstance().getReference("following");

        followButton = findViewById(R.id.followUserButton);
        unfollowButton = findViewById(R.id.unfollowUserButton);


        usersList = new ArrayList<>();

    }
    @Override
    protected void onStart() {
        super.onStart();
        //hides unfollow button if there is no following list accociated with this account
        if(usersList.size() == 0) {unfollowButton.setVisibility(View.INVISIBLE);}
        //Get the user tree data
        Query query = usersDatabase.orderByChild("userID").equalTo(UID);
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
                //checks if the user has actually entered a bio, if not then a placeholder is added.
                if (user.getBio().equals("")) {
                    bioText.setText("This user has not added any information about themselves yet!");
                }
                //set Profile Picture
                Picasso.get()
                        .load(user.getProfilePicture())
                        .noPlaceholder()
                        .into(ppImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewUserProfile.this, "There was an error regarding this account", Toast.LENGTH_SHORT).show();
            }
        });
        //in the following tree, gets the current users tree.
        Query followingRef = followingDatabase.child(currentUserUID);
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users: dataSnapshot.getChildren()) {
                    //gets the children in this tree
                    //adds the IDs to this usersID local variable
                    String thisUserID = users.getKey();
                    //adds the local to the list
                    if(thisUserID!=null) usersList.add(thisUserID);
                    //if this current profiles id is contained in the clients following list then ui will react;
                    if (usersList.contains(UID)) {
                        showUnfollow();
                    }
                    else {
                        showFollow();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void followUser(View view) {
        followingDatabase.child(currentUserUID).child(UID).setValue(user.getUserName());
        clearList();
    }
    public void unFollowUser(View view){
        followingDatabase.child(currentUserUID).child(UID).removeValue();
        clearList();
    }
    public void clearList() {
        usersList.clear();
    }
    public void showFollow() {
        followButton.setVisibility(View.VISIBLE);
        unfollowButton.setVisibility(View.INVISIBLE);
    }
    public void showUnfollow() {
        unfollowButton.setVisibility(View.VISIBLE);
        followButton.setVisibility(View.INVISIBLE);

    }


    public void reportUser() {}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        usersList.clear();
        finish();
    }
}
