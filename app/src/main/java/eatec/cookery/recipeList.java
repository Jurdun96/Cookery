package eatec.cookery;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jurdun-PC on 18/02/2020.
 */

class recipeList extends ArrayAdapter<recipe> {
    private Activity context;
    private List<recipe> recipes;

    public recipeList(Activity context, List<recipe> recipes){
        super(context, R.layout.fragment_row,recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Init inflater
        LayoutInflater inflater = context.getLayoutInflater();
        //Row
        View listViewItem = inflater.inflate(R.layout.fragment_row,null,true);
        //Row objects
        TextView titleText = (TextView) listViewItem.findViewById(R.id.titleText);
        TextView descriptionText = (TextView) listViewItem.findViewById(R.id.descriptionText);
        TextView recipeID = (TextView) listViewItem.findViewById(R.id.recipeIDTextView);
        ImageView recipeImage = listViewItem.findViewById(R.id.rowImage);
        //init Recipe
        recipe recipe = recipes.get(position);
        //Set Data
        recipeImage.setImageResource(R.drawable.veganimage);
        recipeID.setText(recipe.getRecipeID());
        titleText.setText(recipe.getRecipeName());
        descriptionText.setText(recipe.getRecipeDescription());
        //TODO If user has uploaded an image, then use that, else then use default
        return listViewItem;
    }
}
