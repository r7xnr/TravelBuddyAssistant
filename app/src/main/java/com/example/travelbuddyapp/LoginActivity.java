package com.example.travelbuddyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    FirebaseAuth fauth;
    EditText passwordField, emailField;
    TextView login;
    TextView registerBtn;
    TextView forgotBtn;
    ImageButton imgButton;
    ProgressBar progress;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


            // Firebase
            fauth = FirebaseAuth.getInstance();

            if (fauth.getCurrentUser() != null) {
                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                finish();
            }

            setContentView(R.layout.activity_login);

            emailField = findViewById(R.id.emailTxt);
            passwordField = findViewById(R.id.pwfield);
            login = findViewById(R.id.letsgotxt);
            forgotBtn = findViewById(R.id.Forgotpwtxt);
            registerBtn = findViewById(R.id.createacctxt);
            imgButton = findViewById(R.id.imageButton);
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {BackBtn();
                }
            });
            progress = findViewById(R.id.progressBar);
            progress.setVisibility(View.GONE);

            //Get Firebase auth instance
            fauth = FirebaseAuth.getInstance();

            forgotBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                }
            });
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent s = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(s);
                }
            });


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.setVisibility(View.VISIBLE);
                    String email = emailField.getText().toString().trim();
                    String password = passwordField.getText().toString().trim();


                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                        return;
                    }


                    //authenticate user
                    fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    //if (task.isSuccessful()) {
                                    progress.setVisibility(View.GONE);

                                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                        startActivity(intent);
                                        finish();





                                       // }
                                    }
                    });

                }
            });

        }
        public void BackBtn() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }


