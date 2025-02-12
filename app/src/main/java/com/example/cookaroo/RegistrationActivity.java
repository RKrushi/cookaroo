package com.example.cookaroo;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLoginRedirect;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Setup Toolbar with back arrow
        Toolbar toolbar = findViewById(R.id.toolbarRegistration);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // Show the back arrow in the toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register");
        }

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        // The "users" node will be created if not already existing in the database
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Bind views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginRedirect = findViewById(R.id.tvLoginRedirect);
        progressBar = findViewById(R.id.progressBar);

        // Animate the registration form views
       Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        etName.startAnimation(slideInRight);
        etEmail.startAnimation(slideInRight);
        etPassword.startAnimation(slideInRight);
        btnRegister.startAnimation(fadeIn);
        tvLoginRedirect.startAnimation(fadeIn);

        // Handle Register Button click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Redirect to Login Page when clicking the TextView
        tvLoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    // Handle toolbar back arrow click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerUser() {
        // Get user input
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password should be at least 6 characters long");
            etPassword.requestFocus();
            return;
        }

        // Show ProgressBar and disable the register button
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        // Create user with FirebaseAuth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                        if (task.isSuccessful()) {
                            // Registration succeeded, now store additional details in the database
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                // Create a HashMap with registration details
                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("name", name);
                                userMap.put("email", email);
                                // Add more details if needed (e.g., profileImage, bio, etc.)

                                // Save user details under "users/userId" node
                                usersRef.child(userId).setValue(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> dbTask) {
                                                if (dbTask.isSuccessful()) {
                                                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                                    // Navigate to HomeActivity (or any other activity)
                                                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegistrationActivity.this, "Failed to store user data: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Registration failed
                            Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
