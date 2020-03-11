package eatec.cookery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdaptor extends RecyclerView.Adapter<RecipeAdaptor.ViewHolder> {


    private List<recipe> mRecipes;
    private ArrayList<String> mKeys = new ArrayList<>();

    private Context mContext;
    private String mTagList;
    private Query mQuery;
    private EditText mSearchBar;

    private DatabaseReference userRef;
    private user mUser;

    public RecipeAdaptor(List<recipe> recipes, Query query, String strTagList, EditText searchBar){
        mRecipes = recipes;
        mQuery = query;
        mSearchBar = searchBar;
        mTagList = strTagList;
        mUser = new user();
        userRef = FirebaseDatabase.getInstance().getReference("users");
        mQuery.addChildEventListener(new RecipeAdaptor.RecipeChildEventListener());
    }

    class RecipeChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            final recipe recipe = dataSnapshot.getValue(recipe.class);
            //User search??
            if(mSearchBar.getText().toString().contains("@")) {
                Query query = userRef.orderByKey().equalTo(recipe.getUserID());

                final String newSearchBar = mSearchBar.getText().toString().replace("@","");

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                            user user = userSnapshot.getValue(user.class);
                            if(user.getUserName().toLowerCase().contains(newSearchBar.toLowerCase())) {
                                Log.i("2", user.getUserName().toLowerCase());
                                Log.i("2", newSearchBar.toLowerCase());
                                mRecipes.add(recipe);
                                notifyDataSetChanged();

                                String key = dataSnapshot.getKey();
                                mKeys.add(key);
                            }
                            else {}
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            //Normal Search??
            else {
                if (mTagList.toLowerCase().contains(recipe.getTags().toLowerCase())) {
                    if (recipe.getRecipeName().toLowerCase().contains(mSearchBar.getText().toString().toLowerCase())
                            || recipe.getRecipeDescription().toLowerCase().contains(mSearchBar.getText().toString().toLowerCase()))
                    {
                        mRecipes.add(recipe);
                        notifyDataSetChanged();

                        String key = dataSnapshot.getKey();
                        mKeys.add(key);
                    }
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            recipe recipe = dataSnapshot.getValue(recipe.class);
            String key = dataSnapshot.getKey();

            int index = mKeys.indexOf(key);

            mRecipes.set(index, recipe);

            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int index = mKeys.indexOf(dataSnapshot.getKey());
            mRecipes.remove(index);
            mKeys.remove(index);

            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mRecipeTitle;
        private TextView mRecipeDescription;
        private ImageView mRowImage;
        private CardView mCard;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.titleText);
            mRecipeDescription = (TextView) itemView.findViewById(R.id.descriptionText);
            mRowImage = (ImageView) itemView.findViewById(R.id.rowImage);
            mCard = (CardView) itemView.findViewById(R.id.recipeCard);
        }
    }

    @Override
    public RecipeAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View recipeView = inflater.inflate(R.layout.fragment_row,parent,false);

        RecipeAdaptor.ViewHolder viewHolder = new RecipeAdaptor.ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeAdaptor.ViewHolder holder, final int position) {
        final recipe recipe = mRecipes.get(position);
        //Make the user clickable
        final CardView cardView = holder.mCard;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeID = recipe.getRecipeID();

                Intent mIntent = new Intent(mContext, ViewRecipeActivity.class);
                mIntent.putExtra("recipeID", recipeID);
                mContext.startActivity(mIntent);
            }
        });
        final TextView recipetitle = holder.mRecipeTitle;
        final TextView recipedescription = holder.mRecipeDescription;
        final ImageView imageview = holder.mRowImage;

        recipetitle.setText(recipe.getRecipeName());
        recipedescription.setText(recipe.getRecipeDescription());
        Picasso.get().load(recipe.getRecipeImage()).into(imageview);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

}
