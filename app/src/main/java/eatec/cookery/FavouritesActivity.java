package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //Highlight the home buttons to indicated current page;
        highlightMenuIcon();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        ImageView favouriteButton = findViewById(R.id.favouriteButton);//THIS SELECTED
        favouriteButton.setImageResource(R.drawable.heart_selected);

        ImageView myRecipesButton = findViewById(R.id.myRecipesButton);
        myRecipesButton.setImageResource(R.drawable.book);
    }
    public void openCreatorActivity(View view) {
        startActivity(new Intent(FavouritesActivity.this, CreatorActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openSocialActivity(View view) {
        startActivity(new Intent(FavouritesActivity.this, SocialActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openRecipesActivity(View view) {
        startActivity(new Intent(FavouritesActivity.this, RecipesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openHomeActivity(View view) {
        startActivity(new Intent(FavouritesActivity.this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openFavouritesActivity(View view) {
    }


}
