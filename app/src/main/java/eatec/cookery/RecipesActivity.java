package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        //Highlight the home buttons to indicated current page;
        highlightMenuIcon();

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
