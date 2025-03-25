package com.example.cookaroo;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView appLogo;
    private TextView aboutTitle, aboutDescription, developerName;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Initialize Views
        appLogo = findViewById(R.id.app_logo);
        aboutTitle = findViewById(R.id.about_title);
        aboutDescription = findViewById(R.id.about_description);
        developerName = findViewById(R.id.developer_name);
        btnBack = findViewById(R.id.btn_back);

        // Apply Animations
        animateViews();

        // Back Button Click Event
        btnBack.setOnClickListener(v -> finish());
    }

    private void animateViews() {
        // Fade-in animation for app logo
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);
        appLogo.startAnimation(fadeIn);

        // Slide-in animation for title
        Animation slideIn = new TranslateAnimation(0, 0, -200, 0);
        slideIn.setDuration(1000);
        slideIn.setFillAfter(true);
        aboutTitle.startAnimation(slideIn);

        // Fade-in animation for description
        aboutDescription.setAlpha(0);
        aboutDescription.animate().alpha(1).setDuration(1200).setStartDelay(500);

        // Fade-in animation for developer name
        developerName.setAlpha(0);
        developerName.animate().alpha(1).setDuration(1500).setStartDelay(700);
    }
}
