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

public class recipeList extends ArrayAdapter<recipe> {
    private Activity context;
    List<recipe> recipes;

    public recipeList(Activity context, List<recipe> recipes){
        super(context, R.layout.row,recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.row,null,true);

        TextView titleText = (TextView) listViewItem.findViewById(R.id.titleText);
        TextView descriptionText = (TextView) listViewItem.findViewById(R.id.descriptionText);

        ImageView recipeImage = listViewItem.findViewById(R.id.rowImage);

        recipe recipe = recipes.get(position);
        titleText.setText(recipe.getRecipeName());
        descriptionText.setText(recipe.getRecipeName());
        //TODO recipeImage.setImageResource();

        return listViewItem;
    }
}
