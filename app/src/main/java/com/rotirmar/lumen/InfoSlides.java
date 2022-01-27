package com.rotirmar.lumen;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.rotirmar.lumen.ui.main.SectionsPagerAdapter;
import com.rotirmar.lumen.databinding.ActivityInfoSlidesBinding;

public class InfoSlides extends AppCompatActivity {

    private ActivityInfoSlidesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInfoSlidesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        /*FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void saltar(View view) {
        Intent intent = new Intent(InfoSlides.this, Consumption.class);
        startActivity(intent);
    }
}