package com.example.travelbuddyapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddyapp.Adapter.PinAdapter;
import com.example.travelbuddyapp.Model.Pin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewPinsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String email;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pins);

        recyclerView = findViewById(R.id.pins_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

        db.collection("pins")
                .whereEqualTo("username", email) // <-- This line
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Pin pins[] = new Pin[task.getResult().size()];
                            int counter = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("On Success", document.getId() + " => " + document.toObject(Pin.class));

                                Pin pin = document.toObject(Pin.class);

                                pins[counter] = pin;

                                counter++;

                            }
                            setUpRecycler(pins);
                        } else {
                            Log.d("On failure", "Error getting documents: ", task.getException());
                        }
                    }
                });


        // specify an adapter (see also next example)

//        mAdapter = new PinAdapter(dataset);
//        recyclerView.setAdapter(mAdapter);
    }

    private void setUpRecycler(Pin[] pins){

        System.out.println(pins[0].getName());


        mAdapter = new PinAdapter(pins);
        recyclerView.setAdapter(mAdapter);

    }
}
