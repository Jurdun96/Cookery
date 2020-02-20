package eatec.cookery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SocialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void openCreatorActivity(View view) {
        startActivity(new Intent(SocialActivity.this, CreatorActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openSocialActivity(View view) {
    }
    public void openHomeActivity(View view) {
        startActivity(new Intent(SocialActivity.this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void openRecipesActivity(View view) {
        startActivity(new Intent(SocialActivity.this, RecipesActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
