package com.rotirmar.lumen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView fuera = findViewById(R.id.fuera);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        fuera.startAnimation(myanim);
    }
}