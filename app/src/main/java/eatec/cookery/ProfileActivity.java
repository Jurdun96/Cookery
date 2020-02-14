package eatec.cookery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        TextView bioText = findViewById(R.id.bioText);
        TextView cookeryRank = findViewById(R.id.cookeryRankText);
        TextView username = findViewById(R.id.usernameText);

        username.setText(currentUser.getEmail());
    }

    public void signOut(View view) {
        Toast.makeText(this, "You are now signed out", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        finish();
    }
}
