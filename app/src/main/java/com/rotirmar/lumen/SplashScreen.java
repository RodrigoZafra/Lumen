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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class SplashScreen extends AppCompatActivity {

    ImageView appIcon;
    TextView appName;
    Animation nameAnimation;
    AnimatedVectorDrawableCompat avdC;
    AnimatedVectorDrawable avd;

    private boolean firstTime;
    String CONSUMPTION_DAY_URL;
    String CONSUMPTION_MONTH_URL;
    String CONSUMPTION_YEAR_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String fechaActual = obtenerFechaActual();
        String fechaActualYear = (Integer.parseInt(fechaActual.substring(0,4))-1) + "";
        String fecha5YearBefore = (Integer.parseInt(fechaActual.substring(0,4))-4) + "";
        CONSUMPTION_DAY_URL = "https://apidatos.ree.es/es/datos/demanda/demanda-tiempo-real?start_date=" + fechaActual +"T00:00&end_date=" + fechaActual + "T23:59&time_trunc=hour";
        CONSUMPTION_MONTH_URL = "https://apidatos.ree.es/es/datos/demanda/demanda-tiempo-real?start_date=" + fecha5YearBefore +"T00:00&end_date=" + fechaActualYear + "T23:59&time_trunc=hour";
        CONSUMPTION_YEAR_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=2018-01-01T00:00&end_date=2021-12-31T23:59&time_trunc=year&geo_trunc=electric_system&geo_limit=peninsular&geo_ids=8741";

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
            requestDataWriteDataBase(CONSUMPTION_DAY_URL, "consumptionDayDemanda.json");
            requestDataWriteDataBase(CONSUMPTION_YEAR_URL, "consumptionYearDemanda.json");
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
            requestDataWriteDataBase(CONSUMPTION_DAY_URL, "consumptionDayDemanda.json");
            new File(getFilesDir(), "/" + "consumptionMonthDemanda.json").createNewFile();
            new File(getFilesDir(), "/" + "consumptionDayYearDemanda.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_YEAR_URL, "consumptionYearDemanda.json");

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

    public static String obtenerFechaActual() {
        String formato = "yyyy-MM-dd";
        return obtenerFechaConFormato(formato);
    }

    public static String obtenerFechaConFormato(String formato) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        //sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return sdf.format(date);
    }
}