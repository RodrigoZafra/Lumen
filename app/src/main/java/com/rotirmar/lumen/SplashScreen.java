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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class SplashScreen extends AppCompatActivity {

    ImageView appIcon;
    TextView appName;
    Animation nameAnimation;
    AnimatedVectorDrawableCompat avdC;
    AnimatedVectorDrawable avd;

    private boolean firstTime;

    String URL = "https://apidatos.ree.es/es/datos/demanda/demanda-tiempo-real?start_date=2022-02-15T00:00&end_date=2022-02-15T23:59&time_trunc=hour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* AIMACIONES */
        //Animacion logo
        appIcon = findViewById(R.id.IVicon_animation);
        Drawable drawable = appIcon.getDrawable();

        if (drawable instanceof AnimatedStateListDrawableCompat) {
            avdC = (AnimatedVectorDrawableCompat) drawable;
            avdC.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            avd = (AnimatedVectorDrawable) drawable;
            avd.start();
        }

        //Animacion nombre
        appName = findViewById(R.id.appTittle);
        nameAnimation = AnimationUtils.loadAnimation(this, R.anim.lumen_animation);
        appName.startAnimation(nameAnimation);

        /* PRIMERA VEZ */
        firstTime = readDataBase();
        if (!firstTime) {
            createDataBase();
        } else {
            requestDataWriteDataBase(URL, "consumptionDayDemanda.json");
        }

        /* ABRIR APP */
        openApp(true);

    }

    private boolean readDataBase() {
        firstTime = new File(getFilesDir() + "/data_base.dat").exists();
        return firstTime;
    }

    private void createDataBase() {
        try {
            //Creacion ficheros iniciales
            new File(getFilesDir() + "/data_base.dat").createNewFile();
            //CONSUMPTION
            //DAY
            new File(getFilesDir(), "/" + "consumptionDayDemanda.json").createNewFile();
            requestDataWriteDataBase(URL, "consumptionDayDemanda.json");
            new File(getFilesDir(), "/" + "consumptionDayImpExp.json").createNewFile();
            new File(getFilesDir(), "/" + "consumptionDayCostUni.json").createNewFile();
            new File(getFilesDir(), "/" + "consumptionDayGenRen.json").createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void requestDataWriteDataBase(String url, String archiveName) {

        AsyncHttpClient client = new AsyncHttpClient();
        //client.setResponseTimeout(1000);
        //client.setConnectTimeout(1000);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());
                try {
                    //Escribir el archivo
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(getFilesDir(), "/" + archiveName)));
                    bw.write(response.toString());
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e("ReData", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Status", "Request fail! Status code: " + statusCode);
                Log.d("Request", "Fail response: " + response);
                Log.e("ERROR", e.toString());

                Toast.makeText(SplashScreen.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openApp(boolean locationPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (firstTime == false) {
                    intent = new Intent(SplashScreen.this, InfoSlides.class);
                } else {
                    intent = new Intent(SplashScreen.this, Consumption.class);
                }
                startActivity(intent);
            }
        }, 2000);
    }
}