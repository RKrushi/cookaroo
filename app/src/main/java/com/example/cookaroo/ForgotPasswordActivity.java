package com.example.cookaroo;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonReset;
    private ImageView backButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind Views
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonReset = findViewById(R.id.buttonReset);
        backButton = findViewById(R.id.backButton);

        // Send Reset Link Button Click
        buttonReset.setOnClickListener(v -> sendPasswordResetEmail());

        // Back Button Click
        backButton.setOnClickListener(v -> {
            finish(); // Closes current activity and returns to the previous one
        });
    }

    private void sendPasswordResetEmail() {
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter your registered email");
            return;
        }

        // Send Reset Email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Reset link sent! Check your email.", Toast.LENGTH_LONG).show();
                        finish(); // Close activity after successful email sent
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
