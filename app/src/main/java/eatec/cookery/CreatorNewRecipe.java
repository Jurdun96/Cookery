package eatec.cookery;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreatorNewRecipe extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference recipeDatabase;
    private DatabaseReference postsDatabase;
    private StorageReference mStorageRef;

    private List<String> tags;
    private String recipeID;
    private String upload;
    //constants
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private ImageView uploadRecipeImageButton;
    private ProgressBar mProgressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_new_recipe);

        mProgressSpinner = findViewById(R.id.progressSpinner);
        mProgressSpinner.setVisibility(View.GONE);

        tags = new ArrayList<>();
        tags.clear();
        // get an instance of the authentication
        mAuth = FirebaseAuth.getInstance();
        //get reference: recipes
        recipeDatabase = FirebaseDatabase.getInstance().getReference("recipes");
        mStorageRef = FirebaseStorage.getInstance().getReference("recipeImages");

        //post to followers
        postsDatabase = FirebaseDatabase.getInstance().getReference("posts");
        recipeID = recipeDatabase.push().getKey();

        uploadRecipeImageButton = findViewById(R.id.uploadRecipeImage);
        uploadRecipeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(CreatorNewRecipe.this, CreatorActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

    }
    private void openFileChooser() {
        //Open file explorer for user to upload an image of themselves
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            //Check if everything is okay, and then set the local path of the image as the URI
            mImageUri = data.getData();
            //show progress spinner
            mProgressSpinner.setVisibility(View.VISIBLE);
            //Get the Size of the image
            Cursor returnCursor = getContentResolver().query(mImageUri,null,null,null,null);
            int imageSize = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();

            if(returnCursor.getLong(imageSize) > 5000000) //Profile Picture size limit
            {
                //Fail, upload aborted
                Toast.makeText(this, "5MB Maximum upload", Toast.LENGTH_LONG).show();
            } else {
                //Continue with upload
                uploadFile();
            }

        }
    }
    private String getFileExtension(Uri uri){
        //get the file extension used.
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if(mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(recipeID + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //add a wait on the progress bar of 0.2 seconds so that the user can notice it
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressSpinner.setVisibility(View.INVISIBLE);
                                }
                            }, 200);
                            Toast.makeText(CreatorNewRecipe.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            Picasso.get().load(mImageUri).noPlaceholder().into(uploadRecipeImageButton);
                            //convert uri to string
                            upload = taskSnapshot.getDownloadUrl().toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreatorNewRecipe.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        } else {
            Toast.makeText(this,"No file Selected", Toast.LENGTH_SHORT).show();
        }
    }
    public void setToDatabase(View view) {
        //get by text views
        EditText editRecipeName = findViewById(R.id.newRecipeName);
        EditText editRecipeDescription = findViewById(R.id.newRecipeDescription);
        //pass text views to string
        String recipeName = editRecipeName.getText().toString();
        String recipeDescription = editRecipeDescription.getText().toString();
        Spinner privacySpinner = findViewById(R.id.privacySpinner);
        //TODO privacy spinner
        //TODO upload image

        StringBuilder strTaglistBuilder = new StringBuilder();
        //if tags is empty, then a field is still added
        if (tags.isEmpty()) {
            strTaglistBuilder.append("none");
        }
        for (int i = 0; tags.size() > i; i++) {
            if (i == 0) {
                strTaglistBuilder.append(tags.get(i));
            } else {
                strTaglistBuilder.append(", " + tags.get(i));
            }
        }
        String strTagList = String.valueOf(strTaglistBuilder);

        Log.i("tags:", strTagList);

        Boolean recipeNameOkay = false;
        Boolean recipeDescriptionOkay = false;
        TextView recipeNameText = findViewById(R.id.recipenameTV);
        if(recipeName.length() < 12) {
            Toast.makeText(this, "Oops... the recipe's name is too short!", Toast.LENGTH_SHORT).show();
            recipeNameText.setTextColor(Color.RED);
        }
        else if (recipeName.length() > 12) {
            recipeNameText.setTextColor(Color.DKGRAY);
            recipeNameOkay = true;
        }

        TextView recipeDescriptionText = findViewById(R.id.recipeDescriptionTV);
        if(recipeDescription.length() < 24 ){
            Toast.makeText(this, "Oops... the recipe's description is too short!", Toast.LENGTH_SHORT).show();
            recipeDescriptionText.setTextColor(Color.RED);
        }
        else if (recipeDescription.length() > 24) {
            recipeDescriptionText.setTextColor(Color.DKGRAY);
            recipeDescriptionOkay = true;
        }

        if (recipeNameOkay && recipeDescriptionOkay) {
            addToDatabase(recipeName, recipeDescription, strTagList);
        }
    }
    public void tagsCheckbox(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        //TODO add a clear button to clear all selections
        switch(view.getId()) {
            case R.id.veganCheck:
                if(checked)
                    tags.add("vegan");
                else
                    tags.remove("vegan");
                break;
            case R.id.vegCheck:
                if(checked)
                    tags.add("veg");
                else
                    tags.remove("veg");
                break;
            case R.id.fishCheck:
                if(checked)
                    tags.add("fish");
                else
                    tags.remove("fish");
                break;
            case R.id.normalCheck:
                if (checked)
                    tags.add("normal");
                else
                    tags.remove("normal");
                break;
            case R.id.hardCheck:
                if(checked)
                    tags.add("hard");
                else
                    tags.remove("hard");
                break;
            case R.id.simpleCheck:
                if (checked)
                    tags.add("simple");
                else
                    tags.remove("simple");
                break;
        }
    }
    protected void addToDatabase(String recipeName, String recipeDescription, String strTagList){
        recipe newRecipe = new recipe(recipeID, mAuth.getCurrentUser().getUid(), recipeName, recipeDescription, strTagList, "private", upload);
        //add to database
        recipeDatabase.child(recipeID).setValue(newRecipe);
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Posts post = new Posts(mAuth.getUid(),"I just made a new "+recipeName+" recipe!", upload, recipeID, 0, currentDateTimeString);
        postsDatabase.child(postsDatabase.push().getKey()).setValue(post);
        gotoStepsLayout();
        finish();
    }
    public void gotoStepsLayout() {
        Intent mIntent = new Intent(CreatorNewRecipe.this, StepActivity.class);
        mIntent.putExtra("recipeID", recipeID);
        startActivity(mIntent);
    }

}
