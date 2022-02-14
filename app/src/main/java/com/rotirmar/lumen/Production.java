package com.rotirmar.lumen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
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
        TabLayout tabs = binding.productionTabs;
        tabs.setupWithViewPager(viewPager);

        BottomNavigationView consumptionBottom = findViewById(R.id.production_bottom_navigation);
        consumptionBottom.setSelectedItemId(R.id.productionMenuButton);
        consumptionBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.consumptionMenuButton:
                        Intent intent = new Intent(getApplicationContext(), Consumption.class);
                        startActivity(intent);
                }
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {//Aparece el menu en la barra de arriba
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }
}