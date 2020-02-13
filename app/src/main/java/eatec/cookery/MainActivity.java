package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    protected Boolean isON = false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }
    //A/libc: Fatal signal 6 (SIGABRT), code -1 (SI_QUEUE) in tid 10573 (eatec.cookery), pid 10573 (eatec.cookery)
    //Error occured only when plugged in for debug information, around the time i was trying to get the UI on the home page to update
    //To whether the user was logged in or not.
    public void openCreatorActivity(View view) {
        startActivity(new Intent(MainActivity.this, CreatorActivity.class));
    }
    public void openSocialActivity(View view) {
        //startActivity(new Intent(MainActivity.this, CreatorActivity.class));
    }

    public void openRecipesActivity(View view) {
        startActivity(new Intent(MainActivity.this, RecipesActivity.class));
    }

    public void openLoginActivity(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    protected void onResume() {
        super.onResume();
        TextView username = findViewById(R.id.usernameTextBox);
        TextView login = findViewById(R.id.usernameTextBox);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            username.setText(user.getEmail());
            login.setVisibility(View.INVISIBLE);
            username.setVisibility(View.VISIBLE);
        }
        else {
            login.setVisibility(View.VISIBLE);
            username.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}



