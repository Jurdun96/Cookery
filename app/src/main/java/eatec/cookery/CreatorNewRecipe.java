package eatec.cookery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatorNewRecipe extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    private DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_new_recipe);

        // get an instance of the authentication
        mAuth = FirebaseAuth.getInstance();
        //get reference: recipes
        Database = FirebaseDatabase.getInstance().getReference("recipes");
        // Create the adapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }
    public static class creatorFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static creatorFragment newInstance(int sectionNumber) {
            creatorFragment fragment = new creatorFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                View rootView = inflater.inflate(R.layout.fragment_new_recipe, container, false);
                return rootView;}
            else {
                View rootView = inflater.inflate(R.layout.fragment_creator_new_recipe_builder, container, false);
                return rootView;
            }

        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a creatorFragment (defined as a static inner class below).
            return creatorFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    protected void setToDatabase(View view) {
        EditText editRecipeName = (EditText) findViewById(R.id.newRecipeName);
        EditText editRecipeDescription = (EditText) findViewById(R.id.newRecipeDescription);

        String recipeName = editRecipeName.getText().toString();
        String recipeDescription = editRecipeDescription.getText().toString();

        String tags = "none";

        CheckBox veganCheck = findViewById(R.id.veganCheck);
        if (veganCheck.isChecked()) {
            tags += ", vegan";
        }
        CheckBox gfCheck = findViewById(R.id.gfCheck);
        if (gfCheck.isChecked()) {
            tags += ", gf";
        }
        CheckBox vegCheck = findViewById(R.id.vegCheck);
        if (vegCheck.isChecked()) {
            tags += ", veg";
        }
        CheckBox nutCheck = findViewById(R.id.nutCheck);
        if (nutCheck.isChecked()) {
            tags += ", nuts";
        }
        CheckBox eggCheck = findViewById(R.id.eggCheck);
        if (eggCheck.isChecked()) {
            tags += ", egg";
        }
        CheckBox lactoCheck = findViewById(R.id.lactoCheck);
        if (lactoCheck.isChecked()) {
            tags += ", lacto";
        }
        Spinner privacySpinner = findViewById(R.id.privacySpinner);
        //TODO privacy spinner
        //TODO upload image
        addToDatabase(recipeName, recipeDescription, tags);

        finish();
    }

    protected void addToDatabase(String recipeName, String recipeDescription, String tags){

        //generate key
        String recipeID = Database.push().getKey();
        // create new recipe
        recipe newRecipe = new recipe(recipeID, mAuth.getCurrentUser().getUid(), recipeName, recipeDescription, tags, "private", "R.drawable.cookery_logo_round");
        //add to database
        Database.child(recipeID).setValue(newRecipe);
    }
    
}
