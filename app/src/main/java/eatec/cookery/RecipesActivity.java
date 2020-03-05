package eatec.cookery;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private ListView viewRecipeList;
    private List<recipe> listRecipesList;

    private List<String> tagList;

    private DatabaseReference Database;

    private CardView veganCard, noneCard, fishCard, vegetarianCard;

    private TextView vegText,noneText,fishText,veganText;

    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        //get database
        Database = FirebaseDatabase.getInstance().getReference("recipes");

        //innit the filter cards
        veganCard = findViewById(R.id.veganCard);
        noneCard = findViewById(R.id.noneCard);
        fishCard = findViewById(R.id.fishCard);
        vegetarianCard = findViewById(R.id.vegCard);

        vegText = findViewById(R.id.vegText);
        vegText.setTextColor(Color.DKGRAY);
        noneText = findViewById(R.id.noneText);
        noneText.setTextColor(Color.GREEN);
        fishText = findViewById(R.id.fishText);
        fishText.setTextColor(Color.DKGRAY);
        veganText = findViewById(R.id.veganText);
        veganText.setTextColor(Color.DKGRAY);

        //init and add null tag as a default
        tagList = new ArrayList<>();

        searchBar = findViewById(R.id.SearchBar);
        //get data on this page through the search bar 1 time.
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    final StringBuilder strTaglistBuilder = new StringBuilder();
                    if (tagList.isEmpty()) {
                        strTaglistBuilder.append("none");
                    }

                    for (int i = 0; tagList.size() > i; i++) {
                        if (i == 0) {
                            strTaglistBuilder.append(tagList.get(i));
                        } else {
                            strTaglistBuilder.append(", " + tagList.get(i));
                        }
                    }
                    Database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Clear previous list
                            listRecipesList.clear();
                            String strTagList = String.valueOf(strTaglistBuilder);
                            //Iterate through the nodes
                            for(DataSnapshot recipeSnapshot : dataSnapshot.getChildren()){
                                //get recipe
                                recipe recipe = recipeSnapshot.getValue(recipe.class);
                                //if the recipe matches tags that the user has selceted and if it matches the search text;
                                if (strTagList.toLowerCase().contains(recipe.getTags().toLowerCase())) {
                                    if (recipe.getRecipeName().toLowerCase().contains(searchBar.getText().toString().toLowerCase())
                                            || recipe.getRecipeDescription().toLowerCase().contains(searchBar.getText().toString().toLowerCase()))
                                    {
                                        //add to list if it matches the search
                                        listRecipesList.add(recipe);
                                    }
                                }



                            }

                            //create Adapter
                            recipeList recipesAdapter = new recipeList(RecipesActivity.this, listRecipesList);
                            //Attatch adapter to list view
                            viewRecipeList.setAdapter(recipesAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    return true;

                }
                else {

                    return false;

                }
            }
        });
        //Highlight the menu buttons to indicated current page;
        highlightMenuIcon();
        //init list
        listRecipesList = new ArrayList<>();
        //init listView
        viewRecipeList = findViewById(R.id.recipeList);
        viewRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the items container where the ID is stored
                TextView recipeIDTV = view.findViewById(R.id.recipeIDTextView);
                String recipeID = recipeIDTV.getText().toString();

                //open the ViewRecipe activity referencening that recipe ID;
                Intent mIntent = new Intent(RecipesActivity.this, ViewRecipeActivity.class);
                mIntent.putExtra("recipeID", recipeID);
                startActivity(mIntent);
            }
        });
    }
    public void setVeganCard(View v) {
        if (tagList.contains("vegan")){
            veganText.setTextColor(Color.DKGRAY);
            tagList.remove("vegan");
        } else {
            veganText.setTextColor(Color.GREEN);
            noneText.setTextColor(Color.DKGRAY);
            tagList.add("vegan");
            tagList.remove("none");
        }
    }
    public void setVegetarianCard(View v) {
        if (tagList.contains("veg")){
            vegText.setTextColor(Color.DKGRAY);
            tagList.remove("veg");
        } else {
            vegText.setTextColor(Color.GREEN);
            noneText.setTextColor(Color.DKGRAY);
            tagList.add("veg");
            tagList.remove("none");
        }
    }
    public void setNoneCard(View v) {
        tagList.clear();

        veganText.setTextColor(Color.DKGRAY);
        vegText.setTextColor(Color.DKGRAY);
        fishText.setTextColor(Color.DKGRAY);
        noneText.setTextColor(Color.GREEN);

        tagList.add("none");

    }
    public void setFishCard(View v) {
        if (tagList.contains("fish")){
            fishText.setTextColor(Color.DKGRAY);
            tagList.remove("fish");
        } else {
            fishText.setTextColor(Color.GREEN);
            noneText.setTextColor(Color.DKGRAY);
            tagList.add("fish");
            tagList.remove("none");
        }
    }

    public void highlightMenuIcon() {
        ImageView socialButton = findViewById(R.id.socialButton);
        socialButton.setImageResource(R.drawable.friends);

        ImageView searchButton = findViewById(R.id.searchButton);//THIS SELECTED
        searchButton.setImageResource(R.drawable.search_selected);

        ImageView homeButton = findViewById(R.id.homeButton);
        homeButton.setImageResource(R.drawable.home_icon);

        ImageView favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setImageResource(R.drawable.heart);

        ImageView myRecipesButton = findViewById(R.id.myRecipesButton);
        myRecipesButton.setImageResource(R.drawable.book);
    }
    public void openCreatorActivity(View view) {
        startActivity(new Intent(RecipesActivity.this, CreatorActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openSocialActivity(View view) {
        startActivity(new Intent(RecipesActivity.this, SocialActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openRecipesActivity(View view) {
    }
    public void openHomeActivity(View view) {
        startActivity(new Intent(RecipesActivity.this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openFavouritesActivity(View view) {
        startActivity(new Intent(RecipesActivity.this, FavouritesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
}
