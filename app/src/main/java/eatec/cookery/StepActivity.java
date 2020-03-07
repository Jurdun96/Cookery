package eatec.cookery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class StepActivity extends AppCompatActivity {
    Button addStepButton;
    private ListView viewStepsList;
    private List<step> listStepsList;
    private DatabaseReference Database;
    private String recipeID;
    private stepList stepAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        //getref
        Database = FirebaseDatabase.getInstance().getReference("steps");
        //pass recipeID;
        recipeID = getIntent().getStringExtra("recipeID");

        //init steps list
        listStepsList = new ArrayList<>();
         stepAdapter = new stepList(StepActivity.this, listStepsList);
        //init steps List view
        viewStepsList = findViewById(R.id.stepsList);
        addStepButton = (Button) findViewById(R.id.addStepButton);
        addStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStep();
            }
        });
    }
    protected void addStep() {
        String stepID = Database.push().getKey();
        step newStep = new step(recipeID, stepID, "stepImage", "","");
        Database.child(stepID).setValue(newStep);
        getData();
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    public void getData() {
        //receives all the recipes and adds them to the list
        viewStepsList.setAdapter(stepAdapter);
        Database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                step step = dataSnapshot.getValue(step.class);
                if(step.getRecipeID().equals(recipeID)) {
                    listStepsList.add(step);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                stepAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        viewStepsList.setAdapter(stepAdapter);
        Database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Clear the list
                listStepsList.clear();
                //Iterate through the nodes
                for(DataSnapshot stepSnapshot : dataSnapshot.getChildren()){
                    //get recipe
                    step step = stepSnapshot.getValue(step.class);
                    //add step to list, if it is apart of the same recipe.
                    if(step.getRecipeID().equals(recipeID)) {
                        listStepsList.add(step);
                    }
                }
                //create Adapter
                stepList stepAdapter = new stepList(StepActivity.this, listStepsList);
                //Attatch adapter to listview
                viewStepsList.setAdapter(stepAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
    }

    public void finishCreating(View view) {
        //give xp
        new giveRep(this, "Recipe added: 1 reputation gained!", 1);
        finish();

    }
}
