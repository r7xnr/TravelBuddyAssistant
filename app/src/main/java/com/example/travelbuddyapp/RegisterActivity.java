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

public class RegisterActivity extends AppCompatActivity {
        private FirebaseAuth auth;
        EditText passwordField, emailField;
        TextView registerSubmit;
        ImageButton imgButton;
        ProgressBar progress;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_register);


                emailField =  findViewById(R.id.emailfield);
                passwordField =  findViewById(R.id.pwfield);
                registerSubmit = findViewById(R.id.gotomaptxt);
                imgButton = findViewById(R.id.imageButton);
                imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {BackBtn();
                        }
                });
                progress= findViewById(R.id.progressBar5);
                progress.setVisibility(View.GONE);


                auth = FirebaseAuth.getInstance();

                registerSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                progress.setVisibility(View.VISIBLE);

                                String email = emailField.getText().toString().trim();
                                String password = passwordField.getText().toString().trim();

                                if(TextUtils.isEmpty(email)) {
                                        progress.setVisibility(View.GONE);

                                        emailField.setError("Email is required");
                                        return;
                                }

                                if(TextUtils.isEmpty(password)){
                                        progress.setVisibility(View.GONE);

                                        passwordField.setError("Password is required");
                                        return;
                                }

                                if (password.length() < 6) {
                                        progress.setVisibility(View.GONE);

                                        passwordField.setError("password must be longer than 6");
                                        return;
                                }
                                //data is valid

                                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task){
                                                if(task.isSuccessful()) {
                                                        progress.setVisibility(View.GONE);

                                                        Toast.makeText(RegisterActivity.this, "UserCreated", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                }
                                                else{
                                                        progress.setVisibility(View.GONE);

                                                        Toast.makeText(RegisterActivity.this, "Error - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                        }




                                });

                        }
                });

        }
        public void BackBtn() {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }

}