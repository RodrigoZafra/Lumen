package com.rotirmar.lumen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

//Eliminar archivos residuales de la animacion anterior rotate
public class SplashScreen extends AppCompatActivity {

    ImageView animation;
    AnimatedVectorDrawableCompat avdC;
    AnimatedVectorDrawable avd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*ImageView fuera = findViewById(R.id.fuera);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        fuera.startAnimation(myanim);*/

        animation = findViewById(R.id.IVicon_animation);

        Drawable drawable = animation.getDrawable();
        if (drawable instanceof AnimatedStateListDrawableCompat) {
            avdC = (AnimatedVectorDrawableCompat) drawable;
            avdC.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            avd = (AnimatedVectorDrawable) drawable;
            avd.start();
        }

        openApp(true);

    }

    private void openApp(boolean locationPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}