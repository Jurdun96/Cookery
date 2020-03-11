package eatec.cookery;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainAdaptor extends RecyclerView.Adapter<MainAdaptor.ViewHolder> {

    private List<Posts> mPosts;
    private ArrayList<String> mKeys = new ArrayList<>();

    private Context mContext;
    private DatabaseReference userRef;
    private DatabaseReference postRef;


    public MainAdaptor(List<Posts> posts){
        mPosts = posts;
        userRef = FirebaseDatabase.getInstance().getReference("users");
        postRef =  FirebaseDatabase.getInstance().getReference("posts");
        postRef.addChildEventListener(new MainAdaptor.MainChildEventListener());
    }

    class MainChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Posts post = dataSnapshot.getValue(Posts.class);
            mPosts.add(post);

            notifyDataSetChanged();

            String key = dataSnapshot.getKey();
            mKeys.add(key);
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

        public ViewHolder(View itemView) {
            super(itemView);
            mContentTextView = itemView.findViewById(R.id.contentTextView);
            mPostIDTV = itemView.findViewById(R.id.postIDTV);
            mUserIDTv = itemView.findViewById(R.id.userIDTV);
            mCard = itemView.findViewById(R.id.postCard);

            mUserImage = itemView.findViewById(R.id.PostImageUser);
            mContentImage = itemView.findViewById(R.id.profileImage);
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
        Posts post = mPosts.get(position);
        //Make the user clickable
        final CardView cardView = holder.mCard;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String recipeID = recipe.getRecipeID();
//
//                Intent mIntent = new Intent(mContext, ViewRecipeActivity.class);
//                mIntent.putExtra("recipeID", recipeID);
//                mContext.startActivity(mIntent);
            }
        });

        TextView postContent = holder.mContentTextView;
        TextView userID = holder.mUserIDTv;
        TextView postID = holder.mPostIDTV;

        final ImageView userImage = holder.mUserImage;
        ImageView postImage = holder.mContentImage;

        userID.setText(post.getmUserID());
        postID.setText(mKeys.get(position));
        postContent.setText(post.getmContent());
        Picasso.get().load(post.getImage()).into(postImage);

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
