package eatec.cookery;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder>{

    private List<step> mSteps;
    private ArrayList<String> mKeys = new ArrayList<>();
    private DatabaseReference stepsRef;

    private String mRecipeID;

    public StepAdapter(List<step> steps, String recipeID){
        mSteps = steps;
        mRecipeID = recipeID;
        stepsRef = FirebaseDatabase.getInstance().getReference().child("steps");
        Query stepsQuery = stepsRef.orderByChild("recipeID").equalTo(mRecipeID);
        stepsQuery.addChildEventListener(new StepChildEventListener());
    }

    class StepChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                step step = dataSnapshot.getValue(step.class);
                mSteps.add(step);

                notifyDataSetChanged();

                String key = dataSnapshot.getKey();
                mKeys.add(key);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                step step = dataSnapshot.getValue(step.class);
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);
                mSteps.set(index, step);
                notifyDataSetChanged();
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
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public EditText stepShortDescription;

        private Button saveChangesButton;

        private TextView stepShortDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            stepShortDescription = (EditText) itemView.findViewById(R.id.stepRecipeShortText);

            saveChangesButton = (Button) itemView.findViewById(R.id.saveChangesButton);

            stepShortDescriptionTextView = (TextView) itemView.findViewById(R.id.stepRecipeShortTextTV);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stepView = inflater.inflate(R.layout.fragment_step,parent,false);

        ViewHolder viewHolder = new ViewHolder(stepView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StepAdapter.ViewHolder holder, final int position) {
        final step step = mSteps.get(position);
        final String stepID = step.getStepID();

        final EditText shortD = holder.stepShortDescription;
        final TextView shortDTextView = holder.stepShortDescriptionTextView;

        Button save = holder.saveChangesButton;

        shortD.setText(step.getStepDescription());
        final String oldString = shortD.getText().toString();

        shortD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(oldString.equals(shortD.getText().toString())) {

                }
                else {
                    shortDTextView.setTextColor(Color.RED);
                }

            }
        });
        //Save button;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newShortD = shortD.getText().toString();

                shortDTextView.setTextColor(Color.DKGRAY);

                stepsRef.child(stepID).child("stepDescription").setValue(newShortD);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

}
