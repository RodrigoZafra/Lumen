package com.rotirmar.lumen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import com.rotirmar.lumen.databinding.ActivityProductionBinding;
import com.rotirmar.lumen.ui.main.SectionsPagerAdapterConsumption;

public class Production extends AppCompatActivity {

    private ActivityProductionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapterConsumption sectionsPagerAdapter = new SectionsPagerAdapterConsumption(this, getSupportFragmentManager());
        ViewPager viewPager = binding.productionViewPager;
        viewPager.setAdapter(sectionsPagerAdapter);


        BottomNavigationView consumptionBottom = findViewById(R.id.production_bottom_navigation);
        consumptionBottom.setSelectedItemId(R.id.productionMenuButton);
        consumptionBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.consumptionMenuButton:
                        Intent intent = new Intent(getApplicationContext(), Consumption.class);
                        startActivity(intent);
                        overridePendingTransition(0,R.anim.anim_left);
                }
                return false;
            }
        });
    }
}