package eatec.cookery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

public class CreatorAdaptor extends RecyclerView.Adapter<CreatorAdaptor.ViewHolder> {


    private List<recipe> mRecipes;
    private ArrayList<String> mKeys = new ArrayList<>();
    private DatabaseReference recipeRef;
    private DatabaseReference postsRef;
    private DatabaseReference stepsRef;
    private Context mContext;
    private FirebaseAuth mAuth;

    public CreatorAdaptor(List<recipe> recipes){
        mRecipes = recipes;
        mAuth = FirebaseAuth.getInstance();
        recipeRef = FirebaseDatabase.getInstance().getReference().child("recipes");
        stepsRef = FirebaseDatabase.getInstance().getReference().child("steps");
        postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        Query recipeQuery = recipeRef.orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid());
        recipeQuery.addChildEventListener(new CreatorAdaptor.RecipeChildEventListener());
    }

    class RecipeChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            recipe recipe = dataSnapshot.getValue(recipe.class);
            mRecipes.add(recipe);

            notifyDataSetChanged();

            String key = dataSnapshot.getKey();
            mKeys.add(key);
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
        private ImageView mRowImage, mrecipeReported;
        private CardView mCard;

        private LinearLayout mButtons;
        private Button mEditButton;
        private Button mDeleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.titleText);
            mRecipeDescription = (TextView) itemView.findViewById(R.id.descriptionText);
            mRowImage = (ImageView) itemView.findViewById(R.id.rowImage);
            mCard = (CardView) itemView.findViewById(R.id.recipeCard);
            mrecipeReported = itemView.findViewById(R.id.recipeReportedImage);
            mButtons = (LinearLayout) itemView.findViewById(R.id.cardButtonsLayout);
            mEditButton = (Button) itemView.findViewById(R.id.editButton);
            mDeleteButton = (Button) itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public CreatorAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View recipeView = inflater.inflate(R.layout.fragment_row,parent,false);

        CreatorAdaptor.ViewHolder viewHolder = new CreatorAdaptor.ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CreatorAdaptor.ViewHolder holder, final int position) {
        final recipe recipe = mRecipes.get(position);
        //Make the user clickable
        CardView cardView = holder.mCard;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeID = recipe.getRecipeID();

                Intent mIntent = new Intent(mContext, ViewRecipeActivity.class);
                mIntent.putExtra("recipeID", recipeID);
                mContext.startActivity(mIntent);
            }
        });

        ImageView reportedRecipe = holder.mrecipeReported;
        if(recipe.getReports() >= 5) {
            reportedRecipe.setVisibility(View.VISIBLE);
        }

        LinearLayout buttonsContainer = holder.mButtons;
        buttonsContainer.setVisibility(View.VISIBLE);

        Button deleteButton = holder.mDeleteButton;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove the recipe
                recipeRef.child(recipe.getRecipeID()).removeValue();
                //remove the steps
                Query stepsQuery = stepsRef.orderByChild(recipe.getRecipeID());
                stepsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot stepsSnapshot: dataSnapshot.getChildren()) {
                            step step = stepsSnapshot.getValue(step.class);
                            if(step.getRecipeID().equals(recipe.getRecipeID()))
                                stepsSnapshot.getRef().removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //remove the posts
                Query postsQuery = postsRef.orderByChild("mRecipeID").equalTo(recipe.getRecipeID());
                postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsSnapshot: dataSnapshot.getChildren()) {
                            Posts post = postsSnapshot.getValue(Posts.class);
                            if(post.getmRecipeID().equals(recipe.getRecipeID())) {
                                postsSnapshot.getRef().removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        Button editButton = holder.mEditButton;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeID = recipe.getRecipeID();
                Intent mIntent = new Intent(mContext, CreatorNewRecipe.class);
                mIntent.putExtra("recipeID", recipeID);
                mContext.startActivity(mIntent);
            }
        });
        TextView recipetitle = holder.mRecipeTitle;
        TextView recipedescription = holder.mRecipeDescription;
        ImageView imageview = holder.mRowImage;

        recipetitle.setText(recipe.getRecipeName());
        recipedescription.setText(recipe.getRecipeDescription());
        Picasso.get().load(recipe.getRecipeImage()).into(imageview);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

}
