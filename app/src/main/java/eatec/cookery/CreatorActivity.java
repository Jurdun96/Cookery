package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class CreatorActivity extends AppCompatActivity {

    private RecyclerView viewRecipeList;
    private ArrayList<recipe> recipeList;
    private CreatorAdaptor recipeAdapter;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        //Highlight the home buttons to indicated current page;
        highlightMenuIcon();

        //Init List
        recipeList = new ArrayList<>();
        recipeAdapter = new CreatorAdaptor(recipeList);
        viewRecipeList = findViewById(R.id.creatorRecyclerView);
        viewRecipeList.setHasFixedSize(true);
        viewRecipeList.setAdapter(recipeAdapter);
        viewRecipeList.setLayoutManager(new LinearLayoutManager(this));

        //innit objects
        createButton = findViewById(R.id.addNewRecipe);



        //Function to delete a recipe and corresponding steps
       /* viewRecipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                createButton.setBackgroundColor(getResources().getColor(R.color.googleButtonColor));
                createButton.setText("Delete Recipe");
                final TextView recipeIDTV = view.findViewById(R.id.recipeIDTextView);
                final String recipeID = recipeIDTV.getText().toString();
                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //remove the recipe
                        recipesDatabase.child(recipeID).removeValue();
                        //remove the steps
                        Query stepsQuery = stepsDatabase.orderByChild(recipeID);
                        stepsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot stepsSnapshot: dataSnapshot.getChildren()) {
                                    step step = stepsSnapshot.getValue(step.class);
                                    if(step.getRecipeID().equals(recipeID))
                                        stepsSnapshot.getRef().removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        setCreateButton();
                    }
                });
                return true;
            }
        });*/


        //Default add recipe behaviour
        Button addRecipeButton = (Button) findViewById(R.id.addNewRecipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreatorActivity.this, CreatorNewRecipe.class));
            }
        });
    }

    public void setCreateButton() {
        //If the user has used the delete function, this method returns the button back to normal
        createButton.setBackgroundColor(getResources().getColor(R.color.AccentGenericButtonColor));
        createButton.setText("Create Recipe");
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatorActivity.this, CreatorNewRecipe.class));
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

    @Override
    public void onBackPressed() {
        if(createButton.getText().equals("Delete Recipe")){
            setCreateButton();
        }else {
            super.onBackPressed();
        }
    }
}
