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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    ImageView appIcon;
    TextView appName;
    Animation nameAnimation;
    AnimatedVectorDrawableCompat avdC;
    AnimatedVectorDrawable avd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appIcon = findViewById(R.id.IVicon_animation);
        Drawable drawable = appIcon.getDrawable();

        if (drawable instanceof AnimatedStateListDrawableCompat) {
            avdC = (AnimatedVectorDrawableCompat) drawable;
            avdC.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            avd = (AnimatedVectorDrawable) drawable;
            avd.start();
        }

        appName = findViewById(R.id.appTittle);
        nameAnimation = AnimationUtils.loadAnimation(this, R.anim.lumen_animation);
        appName.startAnimation(nameAnimation);

        leerJSON();
        openApp(true);

    }

    private void leerJSON() {
        String json =
                "{\n" +
                        " \"firstTime\": \"true\"\n" +
                        "}";
        String firstTime = "";
        try {
            //Step 1: Load text as JSON Object:
            JSONObject jsonObject = new JSONObject(json);
            Log.d("APPJSON", "The raw object is: " + jsonObject.toString());
            //Step 2: Get "Menu" object:
            firstTime = jsonObject.getString("firstTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(firstTime);
        toast.show();
    }

    private void openApp(boolean locationPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, InfoSlides.class);
                startActivity(intent);
            }
        }, 2000);
    }
}