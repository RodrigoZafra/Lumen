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

    private String CONSUMPTION_DAY_DEMANDAREAL_URL;
    private String CONSUMPTION_DAY_DEMANDAPORDIA_URL;
    private String CONSUMPTION_MONTH_DEMANDA_URL;
    private String CONSUMPTION_MONTH_PRICE_URL;
    private String CONSUMPTION_YEAR_DEMANDA_URL;
    private String CONSUMPTION_YEAR_PRICE_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String fechaActual = obtenerFechaActual();
        String diaActual = fechaActual.substring(8, 10);
        String mesActual = fechaActual.substring(5, 7);
        int lastMonth = Integer.parseInt(mesActual) - 1;
        String yearActual = fechaActual.substring(0, 4);
        int lastYear = (Integer.parseInt(yearActual) - 1);
        int year5Before = lastYear - 4;

        /*CONSUMPTION*/
        //DAY
        CONSUMPTION_DAY_DEMANDAREAL_URL = "https://apidatos.ree.es/es/datos/demanda/demanda-tiempo-real?start_date=" + fechaActual + "T00:00&end_date=" + fechaActual + "T23:59&time_trunc=hour";
        if (mesActual.equals("01"))
            CONSUMPTION_DAY_DEMANDAPORDIA_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + lastYear + "-12" + "-" + diaActual + "T00:00&end_date=" + fechaActual + "T23:59&time_trunc=day";
        else
            CONSUMPTION_DAY_DEMANDAPORDIA_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + yearActual + "-" + lastMonth + "-" + diaActual + "T00:00&end_date=" + fechaActual + "T23:59&time_trunc=day";
        //MONTH
        if (mesActual.equals("01"))
            CONSUMPTION_MONTH_DEMANDA_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + lastYear + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=month&geo_trunc=electric_system&geo_limit=peninsular&geo_ids=8741";
        else
            CONSUMPTION_MONTH_DEMANDA_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + lastYear + "-" + mesActual + "-01" + "T00:00&end_date=" + yearActual + "-" + lastMonth + "-31" + "T23:59&time_trunc=month&geo_trunc=electric_system&geo_limit=peninsular&geo_ids=8741";
        if (mesActual.equals("01"))
            CONSUMPTION_MONTH_PRICE_URL = "https://apidatos.ree.es/en/datos/mercados/componentes-precio?start_date=" + lastYear + "01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=month";
        else
            CONSUMPTION_MONTH_PRICE_URL = "https://apidatos.ree.es/en/datos/mercados/componentes-precio?start_date=" + lastYear + "-" + mesActual + "-01T00:00&end_date=" + yearActual + "-" + lastMonth + "-31T23:59&time_trunc=month";
        //YEAR
        CONSUMPTION_YEAR_DEMANDA_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + year5Before + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=year";
        CONSUMPTION_YEAR_PRICE_URL = "https://apidatos.ree.es/en/datos/mercados/componentes-precio-energia-cierre-desglose?start_date=" + year5Before + "-01-01T00:00&end_date=" + lastYear + "-12-31T23:59&time_trunc=year";

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
            /*CONSUMPTION*/
            //DAY
            requestDataWriteDataBase(CONSUMPTION_DAY_DEMANDAREAL_URL, "consumptionDayDemandaTiempoReal.json");
            requestDataWriteDataBase(CONSUMPTION_DAY_DEMANDAPORDIA_URL, "consumptionDayDemandaPorDia.json");
            //MONTH
            requestDataWriteDataBase(CONSUMPTION_MONTH_DEMANDA_URL, "consumptionMonthDemanda.json");
            requestDataWriteDataBase(CONSUMPTION_MONTH_PRICE_URL, "consumptionMonthPrice.json");
            //YEAR
            requestDataWriteDataBase(CONSUMPTION_YEAR_DEMANDA_URL, "consumptionYearDemanda.json");
            requestDataWriteDataBase(CONSUMPTION_YEAR_PRICE_URL, "consumptionYearPrice.json");
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
            /*CONSUMPTION*/
            //DAY
            new File(getFilesDir(), "/" + "consumptionDayDemandaTiempoReal.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_DAY_DEMANDAREAL_URL, "consumptionDayDemandaTiempoReal.json");
            new File(getFilesDir(), "/" + "consumptionDayDemandaTiempoReal.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_DAY_DEMANDAPORDIA_URL, "consumptionDayDemandaPorDia.json");
            //MONTH
            new File(getFilesDir(), "/" + "consumptionMonthDemanda.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_MONTH_DEMANDA_URL, "consumptionMonthDemanda.json");
            new File(getFilesDir(), "/" + "consumptionMonthPrice.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_MONTH_PRICE_URL, "consumptionMonthPrice.json");
            //YEAR
            new File(getFilesDir(), "/" + "consumptionYearDemanda.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_YEAR_DEMANDA_URL, "consumptionYearDemanda.json");
            new File(getFilesDir(), "/" + "consumptionYearPrice.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_YEAR_PRICE_URL, "consumptionYearPrice.json");
            /*PRODUCTION*/
            //DAY
            //MONTH
            //YEAR

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
                Log.d("Electricity", "JSON: " + response.toString());
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

                Toast.makeText(SplashScreen.this, "Request Failed" + archiveName, Toast.LENGTH_SHORT).show();
            }
        });
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