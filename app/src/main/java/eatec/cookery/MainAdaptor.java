package eatec.cookery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Collections;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainAdaptor extends RecyclerView.Adapter<MainAdaptor.ViewHolder> {

    private List<Posts> mPosts;
    private ArrayList<String> mKeys = new ArrayList<>();

    private Context mContext;
    private DatabaseReference likesRef;
    private DatabaseReference userRef;
    private DatabaseReference postRef;

    private List<String> likesList;
    private FirebaseAuth mAuth;


    public MainAdaptor(List<Posts> posts){
        mPosts = posts;
        mAuth = FirebaseAuth.getInstance();
        likesRef = FirebaseDatabase.getInstance().getReference("likes");
        userRef = FirebaseDatabase.getInstance().getReference("users");
        postRef =  FirebaseDatabase.getInstance().getReference("posts");
        likesList = new ArrayList<>();
        postRef.addChildEventListener(new MainAdaptor.MainChildEventListener());

    }

    class MainChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Posts post = dataSnapshot.getValue(Posts.class);
            mPosts.add(post);

            String key = dataSnapshot.getKey();
            mKeys.add(key);

            Collections.reverse(mPosts);
            Collections.reverse(mKeys);

            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Posts post = dataSnapshot.getValue(Posts.class);
            String key = dataSnapshot.getKey();

            int index = mKeys.indexOf(key);

            mPosts.set(index, post);

            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int index = mKeys.indexOf(dataSnapshot.getKey());
            mPosts.remove(index);
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

        private CardView mCard;

        private TextView mContentTextView,
                mPostIDTV,
                mUserIDTv;
        private ImageView mUserImage,
                mContentImage;

        private Button mLikeButton,
                mCommentButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mContentTextView = itemView.findViewById(R.id.contentTextView);
            mPostIDTV = itemView.findViewById(R.id.postIDTV);
            mUserIDTv = itemView.findViewById(R.id.userIDTV);
            mCard = itemView.findViewById(R.id.postCard);

            mUserImage = itemView.findViewById(R.id.PostImageUser);
            mContentImage = itemView.findViewById(R.id.postImage);

            mLikeButton = itemView.findViewById(R.id.likePostButton);
            mCommentButton = itemView.findViewById(R.id.commentPostButton);
        }
    }

    @Override
    public MainAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View recipeView = inflater.inflate(R.layout.fragment_post,parent,false);

        MainAdaptor.ViewHolder viewHolder = new MainAdaptor.ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MainAdaptor.ViewHolder holder, final int position) {
        final Posts post = mPosts.get(position);
        final String recipeID = post.getmRecipeID();
        //Make the recipe posts clickable
        final CardView cardView = holder.mCard;
        if(post.getmRecipeID() != null) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(mContext, ViewRecipeActivity.class);
                    mIntent.putExtra("recipeID", recipeID);
                    mContext.startActivity(mIntent);
                }
            });
        }

        TextView postContent = holder.mContentTextView;
        final TextView userID = holder.mUserIDTv;
        final TextView postIDTV = holder.mPostIDTV;

        final ImageView userImage = holder.mUserImage;
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, ViewUserProfile.class);
                mIntent.putExtra("userID", userID.getText().toString());
                mContext.startActivity(mIntent);
            }
        });
        ImageView postImage = holder.mContentImage;
        if(post.getmImage() != null) {
            postImage.setVisibility(View.VISIBLE);
        }
        else{
            postImage.setVisibility(View.GONE);
        }

        //HANDLE likes system
        /*
        * query0 searches the current uses likes
        * adds the keys of those likes to the likesList(Local List)
        * Onclick, the list is searched for the current posts ID; if it is found, then the like will be retracted from both the users likes list
        * and the posts likes.
        * */
        String postLikes = String.valueOf(post.getmLikes());
        final Button likeButton = holder.mLikeButton;

        Query query0 = likesRef.child(mAuth.getCurrentUser().getUid());
        query0.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Clear the list every time data is changed
                likesList.clear();
                for (DataSnapshot likes: dataSnapshot.getChildren()) {
                    //add the key to a local list so that the program can determine whether or add or remove a like
                    String thisLikeID = likes.getKey();
                    likesList.add(thisLikeID);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        likeButton.setText(postLikes);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; likesList.size() > i; i++){
                    Log.i("Likes List pos" + i, likesList.get(i));
                }
                int currentLikes = post.getmLikes();
                if(likesList.contains(postIDTV.getText().toString())) {
                    likesList.remove(postIDTV.getText().toString());
                    likesRef.child(mAuth.getCurrentUser().getUid()).child(postIDTV.getText().toString()).removeValue();
                    Log.i("LIKES","+");
                    postRef.child(postIDTV.getText().toString()).child("mLikes").setValue(currentLikes - 1);

                    new giveRep(mContext,null, -1, userID.getText().toString());

                }
                else {
                    likesList.add(postIDTV.getText().toString());
                    postRef.child(postIDTV.getText().toString()).child("mLikes").setValue(currentLikes + 1);
                    Log.i(mAuth.getCurrentUser().getUid(),"");
                    likesRef.child(mAuth.getCurrentUser().getUid()).child(postIDTV.getText().toString()).setValue("");

                    new giveRep(mContext, null, 1, userID.getText().toString());
                }
            }
        });

        Button commentButton = holder.mCommentButton;

        userID.setText(post.getmUserID());
        postIDTV.setText(mKeys.get(position));
        postContent.setText(post.getmContent());
        Picasso.get().load(post.getmImage()).into(postImage);

        Query query = userRef.child(post.getmUserID());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user user = dataSnapshot.getValue(user.class);
                Picasso.get().load(user.getProfilePicture()).transform(new CropCircleTransformation()).into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}