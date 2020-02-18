package eatec.cookery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreatorActivity extends AppCompatActivity {

    ListView recipeList;
    String title[] = new String[32];
    String description[] = new String[32];
    int images[] = new int[32];
    LinearLayout newRecipeDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        //Sample Row
        title[0] = "Test Title";
        description[0] = "Test description";
        images[0] = R.drawable.cookery_logo_round;

        recipeList = findViewById(R.id.recipeList);

        myAdapter adapter = new myAdapter(this, title, description, images);
        recipeList.setAdapter(adapter);
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreatorActivity.this, "HAHAAAAA", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class myAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        myAdapter (Context c, String title[], String description[], int imgs[]) {
            super(c, R.layout.row, R.id.titleText, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.rowImage);
            TextView rowTitle = row.findViewById(R.id.titleText);
            TextView rowDescription = row.findViewById(R.id.descriptionText);

            images.setImageResource(rImgs[position]);
            rowTitle.setText(rTitle[position]);
            rowDescription.setText(rDescription[position]);

            return row;
        }
    }



    protected void addNewRecipe(View view) {
        newRecipeDialog = findViewById(R.id.newRecipeDialog);

        newRecipeDialog.setVisibility(View.VISIBLE);

        Button addRecipeButton = (Button) findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText recipeTitle = findViewById(R.id.setTitleText);
                EditText recipeDescription = findViewById(R.id.setDescriptionText);

                final String recipeTitleText = recipeTitle.getText().toString();
                final String recipeDescriptionText = recipeDescription.getText().toString();

                updateRecipes(recipeTitleText, recipeDescriptionText);
            }});

    }

    protected void updateRecipes(String T, String D) {
        for (int i = 0; i < 32; i++) {
            if (title[i] == null) {
                title[i] = T;
                description[i] = D;
                images[i] = R.drawable.cookery_logo_round;
                break;
            }
        }
        myAdapter adapter = new myAdapter(this, title, description, images);
        recipeList.setAdapter(adapter);

        newRecipeDialog.setVisibility(View.INVISIBLE);
    }
}
