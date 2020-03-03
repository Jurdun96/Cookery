package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference Users;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Highlight the home buttons to indicated current page;
        highlightMenuIcon();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {getUserDetails();}


    }

    public void getUserDetails() {
        Users = database.getReference("users");

        Users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView username = findViewById(R.id.usernameTextBox);
                TextView rank = findViewById(R.id.cookeryRankTextBox);
                ImageView userImage = findViewById(R.id.userImage);
                //Get the current users Unique ID. Used to find them in the database.
                UID = mAuth.getCurrentUser().getUid();
                //Set their details in the User details container.
                user user = dataSnapshot.child(UID).getValue(user.class);

                username.setText(user.getUserName());
                rank.setText(user.convertCookeryRank());
                Picasso.get()
                        .load(user.getProfilePicture())
                        .placeholder(R.drawable.user)
                        .into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "There was an error regarding your account, contact an administrator.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openCreatorActivity(View view) {
        startActivity(new Intent(MainActivity.this, CreatorActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openSocialActivity(View view) {
        startActivity(new Intent(MainActivity.this, SocialActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openRecipesActivity(View view) {
        startActivity(new Intent(MainActivity.this, RecipesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openHomeActivity(View view) {
    }
    public void openFavouritesActivity(View view) {
        startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    public void highlightMenuIcon() {
        ImageView socialButton = findViewById(R.id.socialButton);
        socialButton.setImageResource(R.drawable.friends);

        ImageView searchButton = findViewById(R.id.searchButton);
        searchButton.setImageResource(R.drawable.search);

        ImageView homeButton = findViewById(R.id.homeButton);
        homeButton.setImageResource(R.drawable.home_icon_selected); //THIS SELECTED

        ImageView favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setImageResource(R.drawable.heart);

        ImageView myRecipesButton = findViewById(R.id.myRecipesButton);
        myRecipesButton.setImageResource(R.drawable.book);
    }

    public void openLoginActivity(View view) {
        if(currentUser != null) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
    @Override
     protected void onResume() {
        super.onResume();
        TextView username = findViewById(R.id.usernameTextBox);
        TextView login = findViewById(R.id.loginTextBox);
        TextView rank = findViewById(R.id.cookeryRankTextBox);
        if (mAuth.getCurrentUser() != null) {getUserDetails();}
        if (currentUser != null) {
            username.setVisibility(View.VISIBLE);
            rank.setVisibility(View.VISIBLE);

            login.setVisibility(View.INVISIBLE);
        }
        else {
            startActivity(new Intent(MainActivity.this, LoginPreActivity.class));
            login.setVisibility(View.VISIBLE);
            username.setVisibility(View.INVISIBLE);
            rank.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }
}



