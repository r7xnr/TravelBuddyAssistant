package com.example.travelbuddyapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.travelbuddyapp.Model.Pin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Random;

public class AddAPinActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 200;
    private static final int IMAGES_CAPTURE_CODE = 1001 ;
    private static final int CAMERA_REQUEST_CODE =1;
    private ImageButton backBtn;
    private EditText nameField;
    private EditText descField;
    private EditText locateField;
    private ImageView cameraOutput;
    private Uri image_uri;
    private String email;
    private Button captureButton;
    private LatLng latLng;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;
    private Button shareBtn;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_apin);

        progress= findViewById(R.id.progressBar4);
        progress.setVisibility(View.GONE);


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

        Log.d("username", email);


        captureButton = findViewById(R.id.captureImage);
        cameraOutput = findViewById(R.id.imageupl);
        backBtn = findViewById(R.id.imageButton);
        shareBtn = findViewById(R.id.button2);
        nameField = findViewById(R.id.editText);
        descField = findViewById(R.id.editText2);
        locateField = findViewById(R.id.editText3);
        final Random random = new Random();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAPinActivity.super.onBackPressed();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://www.youtube.com/watch?v=LpL9akTG4hI where I learnt this

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else{
                        openCamera();

                    }
                }
                else openCamera();
            }
        });

        getDeviceLocation();

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);

                String name1 = nameField.getText().toString().trim();
                String desc = descField.getText().toString().trim();
                String locate = locateField.getText().toString().trim();

                if(TextUtils.isEmpty(name1)) {
                    progress.setVisibility(View.GONE);

                    nameField.setError("Name is required");
                    return;
                }

                if(TextUtils.isEmpty(desc)) {
                    progress.setVisibility(View.GONE);

                    nameField.setError("Description is required");
                    return;
                }

                if(TextUtils.isEmpty(locate)) {
                    progress.setVisibility(View.GONE);

                    nameField.setError("Location is required");
                    return;
                }



                //data is valid

                String name = nameField.getText().toString();
                String description = descField.getText().toString();
                String location = locateField.getText().toString();
                String id = Integer.toString(random.nextInt(100000000));
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                String category = "";

                if(((CheckBox) findViewById(R.id.checkBox)).isChecked()){
                    category = ((CheckBox) findViewById(R.id.checkBox)).getText().toString();

                }
                else if(((CheckBox) findViewById(R.id.checkBox2)).isChecked()){
                    category = ((CheckBox) findViewById(R.id.checkBox2)).getText().toString();
                }
                else if(((CheckBox) findViewById(R.id.checkBox3)).isChecked()){
                    category = ((CheckBox) findViewById(R.id.checkBox3)).getText().toString();
                }
                else if(((CheckBox) findViewById(R.id.checkBox4)).isChecked()){
                    category = ((CheckBox) findViewById(R.id.checkBox4)).getText().toString();
                }
                else{
                    category = ((CheckBox) findViewById(R.id.checkBox5)).getText().toString();
                }

                mStorageRef = FirebaseStorage.getInstance().getReference();
                String imagePath ="images/"+id+".jpg";
                StorageReference riversRef = mStorageRef.child(imagePath);
                riversRef.putFile(image_uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                                System.out.println(downloadUrl);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Han
                            }
                        });

                Pin pin = new Pin(id,name, description, location, email, imagePath, latitude, longitude, new Timestamp(new Date()), category);
                //this is way of setting with your own id, up to you how you want it
                System.out.println(pin.getUsername());
                db.collection("pins").document(id).set(pin);

                progress.setVisibility(View.GONE);
                openMapsActivity();

            }
        });

        getPermissions();

    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGES_CAPTURE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            System.out.println(image_uri);
            cameraOutput.setImageURI(image_uri);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void getDeviceLocation()
    {
        //Log.d(TAG, "getDeviceLocation: Getting current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try
        {
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && task.getResult() != null) {
                        //Log.d(TAG, "onComplete: Found Current Location");
                        Location currentLocation = (Location) task.getResult();
                        savePin(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    }
                    else {
                        //Log.d(TAG,"onComplete: Cannot find Location");
                        Toast.makeText(AddAPinActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch(SecurityException e)
        {
            //Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    private void savePin(LatLng latLng)
    {
        this.latLng = latLng;
        System.out.println("The latitude is "+latLng.latitude + "The longitude is "+ latLng.longitude);
    }


    private void openMapsActivity()
    {
        Intent s = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(s);
    }

    private void getPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
        }
    }


}






