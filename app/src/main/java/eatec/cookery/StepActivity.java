package eatec.cookery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class StepActivity extends AppCompatActivity {
    Button addStepButton;
    private RecyclerView viewStepsList;
    private List<step> listStepsList;
    private DatabaseReference stepsRef;
    private String recipeID;
    private StepAdapter stepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        //getref
        stepsRef = FirebaseDatabase.getInstance().getReference("steps");
        //pass recipeID;
        recipeID = getIntent().getStringExtra("recipeID");

        //init everything for stepView
        listStepsList = new ArrayList<>();
        stepAdapter = new StepAdapter(listStepsList, recipeID);
        viewStepsList = findViewById(R.id.stepRView);
        viewStepsList.setHasFixedSize(true);
        viewStepsList.setAdapter(stepAdapter);
        viewStepsList.setLayoutManager(new LinearLayoutManager(this));

        //Adding a step
        addStepButton = (Button) findViewById(R.id.addStepButton);
        addStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStep();
            }
        });
    }
    protected void addStep() {
        String stepID = stepsRef.push().getKey();
        step newStep = new step(recipeID, stepID, "stepImage", "","");
        stepsRef.child(stepID).setValue(newStep);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void finishCreating(View view) {
        //give xp
        new giveRep(this, "Recipe added: 1 reputation gained!", 1);
        finish();

    }
}
