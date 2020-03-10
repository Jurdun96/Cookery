package eatec.cookery;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private RecyclerView viewRecipeList;
    private List<recipe> listRecipesList;
    private List<String> tagList;
    private RecipeAdaptor recipeAdaptor;

    private CardView veganCard, noneCard, fishCard, vegetarianCard;

    private TextView vegText,noneText,fishText,veganText;

    private EditText searchBar;

    private DatabaseReference recipeRef;

    private String userRecipeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        userRecipeSearch = getIntent().getStringExtra("userRecipeSearch");
        //innit the filter cards
        veganCard = findViewById(R.id.veganCard);
        noneCard = findViewById(R.id.noneCard);
        fishCard = findViewById(R.id.fishCard);
        vegetarianCard = findViewById(R.id.vegCard);

        vegText = findViewById(R.id.vegText);
        vegText.setTextColor(Color.DKGRAY);
        noneText = findViewById(R.id.noneText);
        noneText.setTextColor(getResources().getColor(R.color.genericButtonColor));
        fishText = findViewById(R.id.fishText);
        fishText.setTextColor(Color.DKGRAY);
        veganText = findViewById(R.id.veganText);
        veganText.setTextColor(Color.DKGRAY);

        //init and add null tag as a default
        tagList = new ArrayList<>();
        searchBar = findViewById(R.id.SearchBar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
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
                recipeRef = FirebaseDatabase.getInstance().getReference().child("recipes");

                Query recipeQuery = recipeRef;
                String strTagList = String.valueOf(strTaglistBuilder);

                //init list
                listRecipesList = new ArrayList<>();
                recipeAdaptor = new RecipeAdaptor(listRecipesList, recipeQuery, strTagList,searchBar);
                viewRecipeList = findViewById(R.id.recipeRView);
                viewRecipeList.setHasFixedSize(true);
                viewRecipeList.setLayoutManager(new LinearLayoutManager(RecipesActivity.this));
                viewRecipeList.setAdapter(recipeAdaptor);
            }
        });

        if(userRecipeSearch != null) {
            searchBar.setText(userRecipeSearch);
            viewRecipeList.setAdapter(recipeAdaptor);
        }
        //Highlight the menu buttons to indicated current page;
        highlightMenuIcon();
    }
    public void setVeganCard(View v) {
        if (tagList.contains("vegan")){
            veganText.setTextColor(Color.DKGRAY);
            tagList.remove("vegan");
        } else {
            veganText.setTextColor(getResources().getColor(R.color.genericButtonColor));
            noneText.setTextColor(Color.DKGRAY);
            tagList.add("vegan");
            tagList.remove("none");
        }
        listEmpty();
    }
    public void setVegetarianCard(View v) {
        if (tagList.contains("veg")){
            vegText.setTextColor(Color.DKGRAY);
            tagList.remove("veg");
        } else {
            vegText.setTextColor(getResources().getColor(R.color.genericButtonColor));
            noneText.setTextColor(Color.DKGRAY);
            tagList.add("veg");
            tagList.remove("none");
        }
        listEmpty();
    }
    public void setNoneCard(View v) {
        tagList.clear();

        veganText.setTextColor(Color.DKGRAY);
        vegText.setTextColor(Color.DKGRAY);
        fishText.setTextColor(Color.DKGRAY);
        noneText.setTextColor(getResources().getColor(R.color.genericButtonColor));

        tagList.add("none");

    }
    public void setFishCard(View v) {
        if (tagList.contains("fish")){
            fishText.setTextColor(Color.DKGRAY);
            tagList.remove("fish");
        } else {
            fishText.setTextColor(getResources().getColor(R.color.genericButtonColor));
            noneText.setTextColor(Color.DKGRAY);
            tagList.add("fish");
            tagList.remove("none");
        }
        listEmpty();
    }
    public void listEmpty() {
        //Checks that if the list is empty then it will set the filter to none UI;
        if(tagList.isEmpty()){
            noneText.setTextColor(getResources().getColor(R.color.genericButtonColor));
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
