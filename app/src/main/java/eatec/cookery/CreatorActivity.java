package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreatorActivity extends AppCompatActivity {

    private ListView viewRecipeList;
    private List<recipe> listRecipesList;
    private DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        Database = FirebaseDatabase.getInstance().getReference("recipes");
        //Init List
        listRecipesList = new ArrayList<>();
        //Init ListView
        viewRecipeList = findViewById(R.id.recipeList);
        //Items in recipe list clickable
        viewRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreatorActivity.this, "HAHAAAAA", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        Database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Clear previous list
                listRecipesList.clear();

                //Iterate through the nodes
                for(DataSnapshot recipeSnapshot : dataSnapshot.getChildren()){
                    //get recipe
                    recipe recipe = recipeSnapshot.getValue(recipe.class);
                    //add to list
                    listRecipesList.add(recipe);
                }
                //create Adapter
                recipeList recipesAdapter = new recipeList(CreatorActivity.this, listRecipesList);
                //Attatch adapter to listview
                viewRecipeList.setAdapter(recipesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
