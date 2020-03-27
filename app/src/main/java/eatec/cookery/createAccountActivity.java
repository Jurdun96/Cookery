package eatec.cookery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private DatabaseReference followingDatabase;
    private DatabaseReference likesDatabase;
    private DatabaseReference favouritesDatabase;

    private List<String> usernames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");
        followingDatabase = FirebaseDatabase.getInstance().getReference("following");
        likesDatabase = FirebaseDatabase.getInstance().getReference("likes");
        favouritesDatabase = FirebaseDatabase.getInstance().getReference("favourites");

        //get usernames to check if they are present or not
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    user user = users.getValue(user.class);
                    usernames.add(user.getUserName().toLowerCase());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createAccount(View view) {
        //Views
        EditText objEmail = findViewById(R.id.emailTextBox);
        EditText objUsername = findViewById(R.id.userNameTextBox);
        EditText objPassword = findViewById(R.id.passwordTextBox);
        //Strings
        final String email = objEmail.getText().toString();
        final String username = objUsername.getText().toString();
        final int cookeryRank = 0;
        final String password = objPassword.getText().toString();

        //check if username is taken
        if(checkUsername(username)) {
            //Create Account class
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(createAccountActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                        //Create account in authentication
                        FirebaseUser user = mAuth.getCurrentUser();
                        //GET Unique ID.
                        String userID = mAuth.getCurrentUser().getUid();
                        //create and link account in database to authentication details.
                        addDetailsToDatabase(userID, email, "", username, cookeryRank);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(createAccountActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean checkUsername(String username) {
        String lowerCaseUsername = username.toLowerCase();
        if(usernames.contains(lowerCaseUsername)) {
            Toast.makeText(createAccountActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    //Add to database method
    public void addDetailsToDatabase (String userID, String email, String profilePicture, String username, int cookeryRank) {
        //create a new user object
        Map<String,String> following = new HashMap<>();
        user newUser = new user(userID, email, username, profilePicture, "", following, cookeryRank, 0);
        //add that user object to the database
        database.child(userID).setValue(newUser);
        followingDatabase.child(userID).child("Maao6NbuS2fzbhTMgpVLJkU02Df1").setValue("Cookery");
        favouritesDatabase.child(userID).child("Default").setValue("Default");

    }
}
