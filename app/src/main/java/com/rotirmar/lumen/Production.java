package com.rotirmar.lumen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rotirmar.lumen.databinding.ActivityProductionBinding;
import com.rotirmar.lumen.ui.main.SectionsPagerAdapterConsumption;
import com.rotirmar.lumen.ui.main.SectionsPagerAdapterProduction;

public class Production extends AppCompatActivity {

    private ActivityProductionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapterProduction sectionsPagerAdapter = new SectionsPagerAdapterProduction(this, getSupportFragmentManager());
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
                        overridePendingTransition(0,R.anim.anim_left);
                }
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {//Aparece el menu en la barra de arriba
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//Gestiona las acciones sobre los botones
        int id = item.getItemId();
        if (id == R.id.favorite) {
            Toast toast = Toast.makeText(this, "Has eleigo esta página como principal!", Toast.LENGTH_LONG);
            toast.show();
        }

        if (id == R.id.comoFunciona) {
            Intent intent = new Intent(Production.this, InfoSlides.class);
            startActivity(intent);
        }
        if (id == R.id.sobreNosotros) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://github.com/RodrigoZafra/Lumen"));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}