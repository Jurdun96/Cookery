package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); 
        mAuth = FirebaseAuth.getInstance();

        //Google Signin
        SignInButton googleSignIn = (SignInButton) findViewById(R.id.GoogleSignin);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GoogleSignInActivity.class);
                view.getContext().startActivity(intent);
            }});

        //Regular login
        EditText objEmail = findViewById(R.id.email);
        EditText objPassword = findViewById(R.id.password);
        final String email = objEmail.getText().toString();
        final String password = objPassword.getText().toString();

        //Listener for the sign in button
        Button signIn = (Button) findViewById(R.id.email_sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateInput(email, password);
            }});
    }

    private void authenticateInput(String email, String password) {
        if (checkExistingLogin() == false) {
            if (checkEmail(email) == true) {
                if(checkPassword(password) == true) {
                    attemptLogin();
                }
            }
        } else {
            Toast.makeText(this, "user already logged in", Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean checkExistingLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        }
        else {
            return false;
        }
    }
    private Boolean checkEmail(String email) {
        if (email != null) {return true;} else {
            Toast.makeText(this, "Email address is no valid", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    private Boolean checkPassword(String password) {
        if (password != null) {return true;} else {
            Toast.makeText(this, "Password field is blank", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void attemptLogin() {
        final EditText objEmail = findViewById(R.id.email);
        final EditText objPassword = findViewById(R.id.password);
        String email = objEmail.getText().toString();
        String password = objPassword.getText().toString();
        //Attempt login with credentials
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Welcome back!",
                                    Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //set signin fields to empty, indicating login was successful along with toast
                            objEmail.setText("");
                            objPassword.setText("");

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Invalid email address or password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void openCreateAccount(View view){
        startActivity(new Intent(LoginActivity.this, createAccountActivity.class));
    }
}


