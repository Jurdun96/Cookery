package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    protected Boolean isON = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton searchButt = (FloatingActionButton) findViewById(R.id.SearchButton);
        final FloatingActionButton creatorButt = (FloatingActionButton) findViewById(R.id.CreatorButton);
        final FloatingActionButton favouriteButt = (FloatingActionButton) findViewById(R.id.FavouritesButton);
        final FloatingActionButton navButt = (FloatingActionButton) findViewById(R.id.NavigationButton);
        final FloatingActionButton loginButt = (FloatingActionButton) findViewById(R.id.LoginButton);
        searchButt.setVisibility(View.INVISIBLE);
        creatorButt.setVisibility(View.INVISIBLE);
        favouriteButt.setVisibility(View.INVISIBLE);

        navButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isON) {
                    searchButt.setVisibility(View.INVISIBLE);
                    creatorButt.setVisibility(View.INVISIBLE);
                    favouriteButt.setVisibility(View.INVISIBLE);
                    isON = false;
                }
                else {
                    searchButt.setVisibility(View.VISIBLE);
                    creatorButt.setVisibility(View.VISIBLE);
                    favouriteButt.setVisibility(View.VISIBLE);
                    isON = true;
                }
            }});

        creatorButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreatorActivity.class);
                view.getContext().startActivity(intent);
            }});
        searchButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecipesActivity.class);
                view.getContext().startActivity(intent);
            }});
        favouriteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FavouritesActivity.class);
                view.getContext().startActivity(intent);
            }});
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(intent);
            }});
    }
}
