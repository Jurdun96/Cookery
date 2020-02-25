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
 * Created by Jurdun-PC on 24/02/2020.
 */

public class stepList extends ArrayAdapter<step>{
    private Activity context;
    private List<step> steps;

    public stepList(Activity context, List<step> steps) {
        super(context, R.layout.fragment_step, steps);
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //init Layout inflater
        LayoutInflater inflater = context.getLayoutInflater();
        //step
        View listViewItem = inflater.inflate(R.layout.fragment_step,null,true);
        //step objects
        TextView descriptionText = (TextView) listViewItem.findViewById(R.id.stepRecipeLongText);
        TextView longDescriptionText = (TextView) listViewItem.findViewById(R.id.stepRecipeShortText);
        ImageView stepImage = listViewItem.findViewById(R.id.stepImage);
        //init Recipe
        step step = steps.get(position);
        //Set Data
        descriptionText.setText(step.getStepDescription());
        longDescriptionText.setText(step.getStepLongDescription());
        //TODO If user has uploaded an image, then use that, else then use default
        return listViewItem;

    }
}
