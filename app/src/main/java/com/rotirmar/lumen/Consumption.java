package com.rotirmar.lumen;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.consumptionTabs;
        tabs.setupWithViewPager(viewPager);

    }
}