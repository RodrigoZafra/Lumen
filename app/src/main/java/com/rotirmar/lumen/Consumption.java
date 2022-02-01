package com.rotirmar.lumen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rotirmar.lumen.ui.main.SectionsPagerAdapter;
import com.rotirmar.lumen.databinding.ActivityConsumptionBinding;
import com.rotirmar.lumen.ui.main.SectionsPagerAdapterConsumption;

public class Consumption extends AppCompatActivity {

    private ActivityConsumptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConsumptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapterConsumption sectionsPagerAdapter = new SectionsPagerAdapterConsumption(this, getSupportFragmentManager());
        ViewPager viewPager = binding.consumptionViewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.consumptionTabs;
        tabs.setupWithViewPager(viewPager);

        BottomNavigationView consumptionBottom = findViewById(R.id.consumption_bottom_navigation);
        consumptionBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.productionMenuButton:
                        Intent intent = new Intent(getApplicationContext(), Production.class);
                        startActivity(intent);
                }
                return false;
            }
        });
    }
}