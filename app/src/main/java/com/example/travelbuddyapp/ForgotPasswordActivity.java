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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageButton imgButton;
    private EditText emailField;
    private TextView resetmetxt;
    private FirebaseAuth auth;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailField = findViewById(R.id.emailField);
        resetmetxt = findViewById(R.id.resetmetxt);
        imgButton = findViewById(R.id.imageButton);
        progress= findViewById(R.id.progressBar2);
        progress.setVisibility(View.GONE);


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackBtn();
            }
        });


        auth = FirebaseAuth.getInstance();

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackBtn();
            }
        });

        resetmetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                String email = emailField.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    progress.setVisibility(View.GONE);

                    Toast.makeText(getApplication(), "Enter your registered email", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.setVisibility(View.GONE);

                            Toast.makeText(ForgotPasswordActivity.this, "We have sent you instructions to reset your password", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            progress.setVisibility(View.GONE);

                            Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
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
