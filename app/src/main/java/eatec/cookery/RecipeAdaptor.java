package eatec.cookery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdaptor extends RecyclerView.Adapter<RecipeAdaptor.ViewHolder> {


    private List<recipe> mRecipes;
    private ArrayList<String> mKeys = new ArrayList<>();

    private Context mContext;
    private FirebaseAuth mAuth;
    private String mTagList;
    private Query mQuery;

    public RecipeAdaptor(List<recipe> recipes, Query query, String strTagList){
        mRecipes = recipes;
        mQuery = query;
        mTagList = strTagList;
        mAuth = FirebaseAuth.getInstance();

        mQuery.addChildEventListener(new RecipeAdaptor.RecipeChildEventListener());
    }

    class RecipeChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            recipe recipe = dataSnapshot.getValue(recipe.class);
            if (mTagList.toLowerCase().contains(recipe.getTags().toLowerCase())) {
                mRecipes.add(recipe);
                notifyDataSetChanged();

                String key = dataSnapshot.getKey();
                mKeys.add(key);
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
