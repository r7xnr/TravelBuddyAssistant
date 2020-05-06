package com.example.travelbuddyapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelbuddyapp.Model.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewAPinActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private Button backbtn;
    private Button title;
    private TextView title2;
    private TextView desc;
    private TextView loc;
    private RatingBar ratingBar;
    private String thePinID;
    private TextView cate;
    private String email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_apin);

        title = findViewById(R.id.titlestring);
        title2 = findViewById(R.id.textView2);
        desc = findViewById(R.id.textView3);
        loc = findViewById(R.id.textView4);
        cate = findViewById(R.id.textView5);
        ratingBar = findViewById(R.id.ratingBar);
        backbtn = findViewById(R.id.button3);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { ViewAPinActivity.super.onBackPressed();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            email = user.getEmail();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        Bundle bundle = (Bundle) getIntent().getExtras().get("pinInfo");

        String name = bundle.getString("name");
        String pinID = bundle.getString("id");
        thePinID = pinID;
        String location = bundle.getString("location");
        String description = bundle.getString("description");
        String image = bundle.getString("image");
        String cat = bundle.getString("cat");
        // get averaged rating from ratingdatabase float rating = bundle.getstring()

        DocumentReference docRef = db.collection("rating").document(thePinID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Success", "DocumentSnapshot data: " + document.getData());

                        Rating rating = document.toObject(Rating.class);

                        System.out.println(rating.getTotalRatings());

                        float pinRating = rating.getTotalRatings()/ rating.getNoOfRating();

                        ((TextView) findViewById(R.id.rating)).setText(String.valueOf(pinRating));

                        System.out.println(rating.getRaters());

                        if(!rating.getRaters().contains(email)) findViewById(R.id.ratingBar).setVisibility(View.VISIBLE);

                    } else {
                        Log.d("Doesn't exist", "No such document");
                        findViewById(R.id.ratingBar).setVisibility(View.VISIBLE);

                    }
                } else {
                    Log.d("Fail", "get failed with ", task.getException());
                }
            }
        });

        Log.d("name", name);
        Log.d("location", location);
        Log.d("description", description);
        Log.d("cat", cat);


        title.setText(name);
        title2.setText(name);
        desc.setText(description);
        loc.setText(location);
        cate.setText(cat);

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if user has not rated database
            //show rating bar

        db.collection("rating");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
             public void onRatingChanged(RatingBar ratingBar, final float v, boolean b) {
            //if(b)
            //SEND RATING TO DATABASE
            //hide rating bar
                System.out.println(v);

                DocumentReference docRef = db.collection("rating").document(thePinID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Success", "DocumentSnapshot data: " + document.getData());

                                Rating rating = document.toObject(Rating.class);

                                rating.addNoOfRating();
                                rating.addTotalRating(v);
                                List<String> raters = rating.getRaters();

                                raters.add(email);

                                db.collection("rating").document(thePinID).set(rating);

                                float pinRating = rating.getTotalRatings()/ rating.getNoOfRating();

                                ((TextView) findViewById(R.id.rating)).setText(String.valueOf(pinRating));
                                findViewById(R.id.ratingBar).setVisibility(View.GONE);

                            } else {
                                Log.d("Doesn't exist", "No such document");

                                List<String> raters = new ArrayList<String>();

                                raters.add(email);


                                Rating rating = new Rating(1, v, raters);

                                db.collection("rating").document(thePinID).set(rating);

                                float pinRating = rating.getTotalRatings()/ rating.getNoOfRating();

                                ((TextView) findViewById(R.id.rating)).setText(String.valueOf(pinRating));
                                findViewById(R.id.ratingBar).setVisibility(View.GONE);

                            }
                        } else {
                            Log.d("Fail", "get failed with ", task.getException());
                        }
                    }
                });

             }
        });
        //end

        mStorageRef = FirebaseStorage.getInstance().getReference(image);
        final File finalLocalFile = localFile;
        mStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        System.out.println("Atleast successful");

                        //System.out.println(taskSnapshot.getBytesTransferred());
                        Bitmap myBitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());

                        if (!finalLocalFile.exists()) {
                            return;
                        }
                        ImageView pinImage = findViewById(R.id.pinImage);
                        pinImage.setImageBitmap(myBitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                      public void onFailure(@NonNull Exception exception) {
                          // Handle failed download
            }
        });

    }


}
