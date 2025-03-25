package com.example.cookaroo;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivFork, ivPlate, ivKnife, ivHeart;
    private TextView tvAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide status bar
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Initialize views
        ivFork = findViewById(R.id.ivFork);
        ivPlate = findViewById(R.id.ivPlate);
        ivKnife = findViewById(R.id.ivKnife);
        ivHeart = findViewById(R.id.ivHeart);
        tvAppName = findViewById(R.id.tvAppName);

        // Start animations
        startAnimations();
    }

    private void startAnimations() {
        // Utensils slide in
        ObjectAnimator forkSlide = ObjectAnimator.ofFloat(ivFork, "translationX", 200f, 0f);
        ObjectAnimator knifeSlide = ObjectAnimator.ofFloat(ivKnife, "translationX", -200f, 0f);

        // Fade in animations
        ObjectAnimator forkFade = ObjectAnimator.ofFloat(ivFork, "alpha", 0f, 1f);
        ObjectAnimator plateFade = ObjectAnimator.ofFloat(ivPlate, "alpha", 0f, 1f);
        ObjectAnimator knifeFade = ObjectAnimator.ofFloat(ivKnife, "alpha", 0f, 1f);

        // Plate scale animation
        ObjectAnimator plateScaleX = ObjectAnimator.ofFloat(ivPlate, "scaleX", 0f, 1f);
        ObjectAnimator plateScaleY = ObjectAnimator.ofFloat(ivPlate, "scaleY", 0f, 1f);

        // Heart animations
        ObjectAnimator heartFade = ObjectAnimator.ofFloat(ivHeart, "alpha", 0f, 1f);
        ObjectAnimator heartBeat = ObjectAnimator.ofFloat(ivHeart, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator heartBeatY = ObjectAnimator.ofFloat(ivHeart, "scaleY", 1f, 1.2f, 1f);

        // Text animation
        ObjectAnimator textFade = ObjectAnimator.ofFloat(tvAppName, "alpha", 0f, 1f);
        ObjectAnimator textSlide = ObjectAnimator.ofFloat(tvAppName, "translationY", 50f, 0f);

        // Create animation set
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                forkSlide, knifeSlide,
                forkFade, plateFade, knifeFade,
                plateScaleX, plateScaleY
        );
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new DecelerateInterpolator());

        // Heart animation set
        AnimatorSet heartSet = new AnimatorSet();
        heartSet.playTogether(heartFade, heartBeat, heartBeatY);
        heartSet.setStartDelay(1000);
        heartSet.setDuration(800);

        // Text animation set
        AnimatorSet textSet = new AnimatorSet();
        textSet.playTogether(textFade, textSlide);
        textSet.setStartDelay(1500);
        textSet.setDuration(800);

        // Play all animations
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playTogether(animatorSet, heartSet, textSet);
        finalSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Log for debugging
                Log.d("SplashActivity", "Animation finished, moving to LoginActivity");

                // Wait for 5 seconds before smoving to LoginActivity
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this,OnboardingActivity.class);
                    startActivity(intent);
                    finish(); // Close SplashActivity
                }, 3000); // 5-second delay
            }
        });
        finalSet.start();
    }
}