package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreatorActivity extends AppCompatActivity {

    private ListView viewRecipeList;
    private ArrayList<recipe> recipeList;
    private DatabaseReference Database;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        //Highlight the home buttons to indicated current page;
        highlightMenuIcon();
        //get recipes from database reference and the log in information
        Database = FirebaseDatabase.getInstance().getReference("recipes");
        mAuth = FirebaseAuth.getInstance();
        //Init List
        recipeList = new ArrayList<>();
        viewRecipeList = findViewById(R.id.listView1);
        viewRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the items container where the ID is stored
                TextView recipeIDTV = view.findViewById(R.id.recipeIDTextView);
                String recipeID = recipeIDTV.getText().toString();

                //open the ViewRecipe activity referencening that recipe ID;
                Intent mIntent = new Intent(CreatorActivity.this, ViewRecipeActivity.class);
                mIntent.putExtra("recipeID", recipeID);
                startActivity(mIntent);
            }
        });
        //Add new Recipe
        FloatingActionButton addRecipeButton = (FloatingActionButton) findViewById(R.id.addNewRecipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreatorActivity.this, CreatorNewRecipe.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //query to just associate the table with that user; Meaning if someone else adds a recipe it on their account
        //it will not affect this list
        Query query = Database.orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Clear previous list
                //recipeList.clear();

                //Iterate through the nodes
                for(DataSnapshot recipeSnapshot : dataSnapshot.getChildren()){

                    //get recipe
                    recipe mrecipe = recipeSnapshot.getValue(recipe.class);
                    recipeList.add(mrecipe);
                    //init and set adapter to view
                    recipeList recipesAdapter = new recipeList(CreatorActivity.this, recipeList);
                    viewRecipeList.setAdapter(recipesAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void highlightMenuIcon() {
        ImageView socialButton = findViewById(R.id.socialButton);
        socialButton.setImageResource(R.drawable.friends);

        ImageView searchButton = findViewById(R.id.searchButton);
        searchButton.setImageResource(R.drawable.search);

        ImageView homeButton = findViewById(R.id.homeButton);
        homeButton.setImageResource(R.drawable.home_icon);

        ImageView favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setImageResource(R.drawable.heart);

        ImageView myRecipesButton = findViewById(R.id.myRecipesButton);//THIS SELECTED
        myRecipesButton.setImageResource(R.drawable.book_selected);
    }
    public void openCreatorActivity(View view) {
    }
    public void openSocialActivity(View view) {
        startActivity(new Intent(CreatorActivity.this, SocialActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openRecipesActivity(View view) {
        startActivity(new Intent(CreatorActivity.this, RecipesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openHomeActivity(View view) {
        startActivity(new Intent(CreatorActivity.this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openFavouritesActivity(View view) {
        startActivity(new Intent(CreatorActivity.this, FavouritesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

}
