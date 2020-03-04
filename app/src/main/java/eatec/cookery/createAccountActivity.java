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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class createAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");
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

        //Create Account class
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                            addDetailsToDatabase(userID, email, password, username, cookeryRank);

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(createAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Add to database method
    public void addDetailsToDatabase (String userID, String email, String profilePicture, String username, int cookeryRank) {
        //create a new user object
        Map<String,String> following = new HashMap<>();
        user newUser = new user(userID, email, username, profilePicture, "", following, cookeryRank);
        //add that user object to the database
        database.child(userID).setValue(newUser);
    }

}
