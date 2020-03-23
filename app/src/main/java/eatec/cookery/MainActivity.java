package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference posts;
    private DatabaseReference Users;
    private Button postButton;
    private EditText postContainer;
    private String UID;

    private MainAdaptor mainAdaptor;
    private RecyclerView listPostsView;
    private List<Posts> listPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Highlight the home buttons to indicated current page;
        highlightMenuIcon();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        posts = database.getReference("posts");
        if (mAuth.getCurrentUser() != null) {getUserDetails();}

        postButton = findViewById(R.id.postButton);
        postContainer = findViewById(R.id.postEditText);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postUpdate();
            }
        });

        listPosts = new ArrayList<>();
        listPostsView = findViewById(R.id.postsRView);
        mainAdaptor = new MainAdaptor(listPosts);
        listPostsView.setHasFixedSize(true);
        listPostsView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        listPostsView.setAdapter(mainAdaptor);
        listPostsView.setNestedScrollingEnabled(false);

    }

    public void getUserDetails() {
        Users = database.getReference("users");

        Users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ImageView userImage = findViewById(R.id.profileImage);
                //Get the current users Unique ID. Used to find them in the database.

                UID = mAuth.getCurrentUser().getUid();
                //Set their details in the User details container.
                user user = dataSnapshot.child(UID).getValue(user.class);
                Picasso.get()
                        .load(user.getProfilePicture())
                        .placeholder(R.drawable.user)
                        .transform(new CropCircleTransformation())
                        .into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "There was an error regarding your account, contact an administrator.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void postUpdate() {
        if(!postContainer.getText().toString().equals("")) {
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
            Posts post = new Posts(mAuth.getUid(), postContainer.getText().toString(), null, null,0, currentDateTimeString);
            posts.push().setValue(post);
            postContainer.setText("");
        }
        else {
            Toast.makeText(this, "You cannot post an empty update.", Toast.LENGTH_SHORT).show();
        }

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

        if (currentUser != null) {
            getUserDetails();
            new checkStrikes(MainActivity.this);
        }
        else {
            startActivity(new Intent(MainActivity.this, LoginPreActivity.class));
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }
}



