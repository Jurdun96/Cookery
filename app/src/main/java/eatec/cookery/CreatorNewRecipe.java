package eatec.cookery;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreatorNewRecipe extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference recipeDatabase;
    private List<String> tags;
    private String recipeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_new_recipe);

        tags = new ArrayList<>();
        tags.clear();
        // get an instance of the authentication
        mAuth = FirebaseAuth.getInstance();
        //get reference: recipes
        recipeDatabase = FirebaseDatabase.getInstance().getReference("recipes");

    }
    public void setToDatabase(View view) {
        //get by text views
        EditText editRecipeName = findViewById(R.id.newRecipeName);
        EditText editRecipeDescription = findViewById(R.id.newRecipeDescription);
        //pass text views to string
        String recipeName = editRecipeName.getText().toString();
        String recipeDescription = editRecipeDescription.getText().toString();
        Spinner privacySpinner = findViewById(R.id.privacySpinner);
        //TODO privacy spinner
        //TODO upload image

        StringBuilder strTaglistBuilder = new StringBuilder();
        //if tags is empty, then a field is still added
        if (tags.isEmpty()) {
            strTaglistBuilder.append("none");
        }
        for (int i = 0; tags.size() > i; i++) {
            if (i == 0) {
                strTaglistBuilder.append(tags.get(i));
            } else {
                strTaglistBuilder.append(", " + tags.get(i));
            }
        }
        String strTagList = String.valueOf(strTaglistBuilder);

        Log.i("tags:", strTagList);

        Boolean recipeNameOkay = false;
        Boolean recipeDescriptionOkay = false;
        TextView recipeNameText = findViewById(R.id.recipenameTV);
        if(recipeName.length() < 12) {
            Toast.makeText(this, "Oops... the recipe's name is too short!", Toast.LENGTH_SHORT).show();
            recipeNameText.setTextColor(Color.RED);
        }
        else if (recipeName.length() > 12) {
            recipeNameText.setTextColor(Color.DKGRAY);
            recipeNameOkay = true;
        }

        TextView recipeDescriptionText = findViewById(R.id.recipeDescriptionTV);
        if(recipeDescription.length() < 24 ){
            Toast.makeText(this, "Oops... the recipe's description is too short!", Toast.LENGTH_SHORT).show();
            recipeDescriptionText.setTextColor(Color.RED);
        }
        else if (recipeDescription.length() > 24) {
            recipeDescriptionText.setTextColor(Color.DKGRAY);
            recipeDescriptionOkay = true;
        }

        if (recipeNameOkay && recipeDescriptionOkay) {
            addToDatabase(recipeName, recipeDescription, strTagList);
        }
    }

    public void tagsCheckbox(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        //TODO add a clear button to clear all selections
        switch(view.getId()) {
            case R.id.veganCheck:
                if(checked)
                    tags.add("vegan");
                else
                    tags.remove("vegan");
                break;
            case R.id.vegCheck:
                if(checked)
                    tags.add("veg");
                else
                    tags.remove("veg");
                break;
            case R.id.fishCheck:
                if(checked)
                    tags.add("fish");
                else
                    tags.remove("fish");
                break;
            case R.id.normalCheck:
                if (checked)
                    tags.add("normal");
                else
                    tags.remove("normal");
                break;
            case R.id.hardCheck:
                if(checked)
                    tags.add("hard");
                else
                    tags.remove("hard");
                break;
            case R.id.simpleCheck:
                if (checked)
                    tags.add("simple");
                else
                    tags.remove("simple");
                break;
        }
    }
    protected void addToDatabase(String recipeName, String recipeDescription, String strTagList){
        recipeID = recipeDatabase.push().getKey();
        // create new recipe
        recipe newRecipe = new recipe(recipeID, mAuth.getCurrentUser().getUid(), recipeName, recipeDescription, strTagList, "private", "R.drawable.cookery_logo_round");
        //add to database
        recipeDatabase.child(recipeID).setValue(newRecipe);

        gotoStepsLayout();
        finish();
    }
    public void gotoStepsLayout() {
        Intent mIntent = new Intent(CreatorNewRecipe.this, StepActivity.class);
        mIntent.putExtra("recipeID", recipeID);
        startActivity(mIntent);
    }

}
