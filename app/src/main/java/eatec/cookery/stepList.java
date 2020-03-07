package eatec.cookery;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jurdun-PC on 24/02/2020.
 * This class is the class responsible for handling the lists of steps that the user will see during the creator phase.
 * It works by setting an array adapter which uses the fragment_steps layout displaying it on a list within the StepActivity
 * On text listeners are on each of the fields, when the user edits one of the fields, the program waits 600ms, and then uploads the
 * data to the database;
 * this refreshes the list TODO change the way the list refreshes in the instance of the creator; perhaps add a delay of 1 minuite before refresh, and or if a new step has been added
 * A timer is responsible for this delay. The setting of data is to a specific path; being the recipe -> step -> long/shortText field.
 */

public class stepList extends ArrayAdapter<step>{
    private Activity context;
    private List<step> steps;
    private DatabaseReference database;
    private Timer timer1,timer2;

    //old
    private TextView descriptionText;
    private TextView longDescriptionText;
    //New
    private String newDescriptionText;
    private String newLongDescriptionText;

    private step step;
    private String stepID;

    public stepList(Activity context, List<step> steps) {
        super(context, R.layout.fragment_step, steps);
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get database
        database = FirebaseDatabase.getInstance().getReference("steps");

        //init Layout inflater
        LayoutInflater inflater = context.getLayoutInflater();

        //step
        View listViewItem = inflater.inflate(R.layout.fragment_step,null,true);

        //step objects
        descriptionText = (TextView) listViewItem.findViewById(R.id.stepRecipeShortText);
        longDescriptionText = (TextView) listViewItem.findViewById(R.id.stepRecipeLongText);
        ImageView stepImage = listViewItem.findViewById(R.id.stepImage);
        //init step
        step = steps.get(position);

        //get stepID
        stepID = step.getStepID();

        //Set Data
        descriptionText.setText(step.getStepDescription());
        longDescriptionText.setText(step.getStepLongDescription());

        //TODO If user has uploaded an image, then use that, else then use default

        //Add listener to descriptionText so that when a user edits the fields, it is uploaded to the same step in the database
        descriptionText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user is typing: reset already started timer (if existing)
                if (timer1 != null) {
                    timer1.cancel();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                timer1 = new Timer();
                timer1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        newDescriptionText = descriptionText.getText().toString();
                        addToDatabase(stepID,"stepDescription", newDescriptionText);
                    }
                }, 600);
            }
        });
        //Add listener to LongDescriptionText so that when a user edits the fields, it is uploaded to the same step in the database
        longDescriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user is typing: reset already started timer (if existing)
                if (timer2 != null) {
                    timer2.cancel();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        String newLongDescriptionText = longDescriptionText.getText().toString();
                        addToDatabase(stepID, "stepLongDescription", newLongDescriptionText);
                    }
                }, 600);
            }
        });
        return listViewItem;
    }
    //Add the data the user is entering to the database; there is a 600ms delay on the period between the user stopping typing and the data being updated.
    private void addToDatabase(String id, String location, String text) {
        database.child(id).child(location).setValue(text);
    }

    private void updateList(List<step> newList) {
        Log.i("adapter", "notified");
        steps.clear();
        steps.addAll(newList);
        this.notifyDataSetChanged();
    }
}
