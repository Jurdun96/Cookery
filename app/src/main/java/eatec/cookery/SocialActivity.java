package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SocialActivity extends AppCompatActivity {

    private ListView viewUserList;
    private List<user> listUserList;

    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        //Highlight the home buttons to indicated current page;
        highlightMenuIcon();

        database = FirebaseDatabase.getInstance().getReference("users");
        listUserList = new ArrayList<>();
        viewUserList = findViewById(R.id.userListLV);

        viewUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SocialActivity.this, "Nope", Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Nope", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void highlightMenuIcon() {
        ImageView socialButton = findViewById(R.id.socialButton);//THIS SELECTED
        socialButton.setImageResource(R.drawable.friends_selected);

        ImageView searchButton = findViewById(R.id.searchButton);
        searchButton.setImageResource(R.drawable.search);

        ImageView homeButton = findViewById(R.id.homeButton);
        homeButton.setImageResource(R.drawable.home_icon);

        ImageView favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setImageResource(R.drawable.heart);

        ImageView myRecipesButton = findViewById(R.id.myRecipesButton);
        myRecipesButton.setImageResource(R.drawable.book);
    }

    @Override
    protected void onStart() {
        super.onStart();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUserList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    user user = userSnapshot.getValue(user.class);
                    listUserList.add(user);
                }

                userList userAdapter = new userList(SocialActivity.this, listUserList);
                viewUserList.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void openCreatorActivity(View view) {
        startActivity(new Intent(SocialActivity.this, CreatorActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openSocialActivity(View view) {
    }
    public void openHomeActivity(View view) {
        startActivity(new Intent(SocialActivity.this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openRecipesActivity(View view) {
        startActivity(new Intent(SocialActivity.this, RecipesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openFavouritesActivity(View view) {
        startActivity(new Intent(SocialActivity.this, FavouritesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
}
