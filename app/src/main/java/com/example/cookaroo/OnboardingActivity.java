package com.example.cookaroo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter onboardingAdapter;
    private Button btnNext, btnSkip;
    private WormDotsIndicator dotsIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
        dotsIndicator = findViewById(R.id.dotsIndicator);

        setupOnboardingItems();

        // Set ViewPager Adapter
        viewPager.setAdapter(onboardingAdapter);
        dotsIndicator.attachTo(viewPager);

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < onboardingAdapter.getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                goToMainActivity();
            }
        });

        btnSkip.setOnClickListener(v -> goToMainActivity());
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        onboardingItems.add(new OnboardingItem(R.drawable.scan_ingredients, "Add Ingredients",
                "Easily Add your available ingredients to find matching recipes instantly."));
        onboardingItems.add(new OnboardingItem(R.drawable.food_image, "Discover Recipes",
                "Explore a variety of new and delicious recipes based on your ingredients."));
        onboardingItems.add(new OnboardingItem(R.drawable.save_favorites, "Save Favorites",
                "Save your favorite recipes for quick access anytime."));

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
