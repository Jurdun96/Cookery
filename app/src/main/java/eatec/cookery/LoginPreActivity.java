package eatec.cookery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginPreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pre);
    }
    public void openLoginActivity(View view) {
            startActivity(new Intent(LoginPreActivity.this, LoginActivity.class));
            finish();
    }
}
