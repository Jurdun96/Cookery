package eatec.cookery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentPost extends AppCompatActivity {

    private Button commentButton;
    private EditText commentTextBox;
    private List<comment> commentList;
    private RecyclerView commentRecycler;
    private CommentAdapter commentAdapter;
    private ImageView postImage;
    private String postID;
    private Posts post;
    private String userID;
    private DatabaseReference postRef;
    private DatabaseReference commentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postID = getIntent().getStringExtra("postID");

        commentButton = findViewById(R.id.CPostCommentButton);
        commentTextBox = findViewById(R.id.CPostCommentContainer);
        postImage = findViewById(R.id.CPostImageView);
        commentRecycler = findViewById(R.id.CPostRView);

        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postID);
        commentRef = FirebaseDatabase.getInstance().getReference("comments");
        post = new Posts();

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Posts.class);
                if(post.getmImage() != null) {
                    postImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(post.getmImage()).into(postImage);
                } else{
                    postImage.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentkey = commentRef.push().getKey();
                comment comment = new comment(userID,commentTextBox.getText().toString());
                commentRef.child(postID).child(commentkey).setValue(comment);
            }
        });

        commentList = new ArrayList<>();
        commentRecycler = findViewById(R.id.CPostRView);
        commentAdapter = new CommentAdapter(commentList, postID);
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setLayoutManager(new LinearLayoutManager(CommentPost.this));
        commentRecycler.setAdapter(commentAdapter);
    }
}
