package eatec.cookery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    //variables used in this activity
    ListView viewRecipeList;
    List<recipe> listRecipesList;
    LinearLayout newRecipeDialog;


    //Firebase Database
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
    }


    //addNewRecipe: Opens the dialog box to allow the user to enter a recipe name and description.
    protected void addNewRecipe(View view) {
        newRecipeDialog = findViewById(R.id.newRecipeDialog);
        //Dialog visible
        newRecipeDialog.setVisibility(View.VISIBLE);
        //Button to confirm the name and description
        final Button addRecipeButton = (Button) findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText recipeTitle = findViewById(R.id.setTitleText);
                EditText recipeDescription = findViewById(R.id.setDescriptionText);

                final String recipeTitleText = recipeTitle.getText().toString();
                final String recipeDescriptionText = recipeDescription.getText().toString();

                addToDatabase(recipeTitleText, recipeDescriptionText);
            }});
    }

    //Add new recipe to database;
    protected void addToDatabase(String title, String description){
        String recipeID = Database.push().getKey();
        recipe newRecipe = new recipe(recipeID, title, description, "R.drawable.cookery_logo_round");
        Database.child(recipeID).setValue(newRecipe);

        newRecipeDialog.setVisibility(View.INVISIBLE);
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
