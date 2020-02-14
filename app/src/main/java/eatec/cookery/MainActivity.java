package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    //A/libc: Fatal signal 6 (SIGABRT), code -1 (SI_QUEUE) in tid 10573 (eatec.cookery), pid 10573 (eatec.cookery)
    //^^^^IDK^^^^

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
        if(currentUser != null) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
     protected void onResume() {
        super.onResume();
        TextView username = findViewById(R.id.usernameTextBox);
        TextView login = findViewById(R.id.loginTextBox);
        TextView rank = findViewById(R.id.cookeryRankTextBox);

        if (currentUser != null) {
            username.setVisibility(View.VISIBLE);
            username.setText(currentUser.getEmail());

            rank.setVisibility(View.VISIBLE);
            rank.setText("Rank: 0");

            login.setVisibility(View.INVISIBLE);
        }
        else {
            login.setVisibility(View.VISIBLE);

            username.setVisibility(View.INVISIBLE);
            rank.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }
}



