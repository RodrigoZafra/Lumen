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

    private boolean secondTime;

    private String CONSUMPTION_DAY_REALDEMAND_URL;
    private String CONSUMPTION_DAY_DEMANDPERDAY_URL;
    private String CONSUMPTION_MONTH_DEMAND_URL;
    private String CONSUMPTION_MONTH_PRICE_URL;
    private String CONSUMPTION_YEAR_DEMAND_URL;
    private String CONSUMPTION_YEAR_PRICE_URL;

    private String PRODUCTION_DAY_GENERATION_STRUCTURE_URL;
    private String PRODUCTION_DAY_RENEWABLE_PROPORTION_URL;
    private String PRODUCTION_DAY_EMISSIONS_PROPORTION_URL;
    private String PRODUCTION_MONTH_RENEWABLE_PROPORTION_URL;
    private String PRODUCTION_MONTH_EMISSIONS_PROPORTION_URL;
    private String PRODUCTION_YEAR_RENEWABLE_PROPORTION_URL;
    private String PRODUCTION_YEAR_EMISSIONS_PROPORTION_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String currentDate = obtenerFechaActual();
        String currentDay = currentDate.substring(8, 10);
        String currentMonth = currentDate.substring(5, 7);
        int lastMonth = Integer.parseInt(currentMonth) - 1;
        String currentYear = currentDate.substring(0, 4);
        int lastYear = (Integer.parseInt(currentYear) - 1);
        int year5Before = lastYear - 4;

        /*-----CONSUMPTION-----*/
        //DAY
        CONSUMPTION_DAY_REALDEMAND_URL = "https://apidatos.ree.es/es/datos/demanda/demanda-tiempo-real?start_date=" + currentDate + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=hour";
        if (currentMonth.equals("01"))
            CONSUMPTION_DAY_DEMANDPERDAY_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + lastYear + "-12-" + currentDay + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=day";
        else
            CONSUMPTION_DAY_DEMANDPERDAY_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + currentYear + "-" + lastMonth + "-" + currentDay + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=day";
        //MONTH
        if (currentMonth.equals("01")) {
            CONSUMPTION_MONTH_DEMAND_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + lastYear + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=month&geo_trunc=electric_system&geo_limit=peninsular&geo_ids=8741";
            CONSUMPTION_MONTH_PRICE_URL = "https://apidatos.ree.es/en/datos/mercados/componentes-precio?start_date=" + lastYear + "01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=month";
        } else {
            CONSUMPTION_MONTH_DEMAND_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + lastYear + "-" + currentMonth + "-01" + "T00:00&end_date=" + currentYear + "-" + lastMonth + "-31" + "T23:59&time_trunc=month&geo_trunc=electric_system&geo_limit=peninsular&geo_ids=8741";
            CONSUMPTION_MONTH_PRICE_URL = "https://apidatos.ree.es/en/datos/mercados/componentes-precio?start_date=" + lastYear + "-" + currentMonth + "-01T00:00&end_date=" + currentYear + "-" + lastMonth + "-31T23:59&time_trunc=month";
        }
        //YEAR
        CONSUMPTION_YEAR_DEMAND_URL = "https://apidatos.ree.es/en/datos/demanda/evolucion?start_date=" + year5Before + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=year";
        CONSUMPTION_YEAR_PRICE_URL = "https://apidatos.ree.es/en/datos/mercados/componentes-precio-energia-cierre-desglose?start_date=" + year5Before + "-01-01T00:00&end_date=" + lastYear + "-12-31T23:59&time_trunc=year";

        /*-----PRODUCTION-----*/
        //DAY
        PRODUCTION_DAY_GENERATION_STRUCTURE_URL = "https://apidatos.ree.es/es/datos/generacion/estructura-generacion?start_date=" + currentDate + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=day";
        if (currentMonth.equals("01")) {
            PRODUCTION_DAY_RENEWABLE_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-renovable-no-renovable?start_date=" + lastYear + "-12-" + currentDay + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=day";
            PRODUCTION_DAY_EMISSIONS_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-estructura-generacion-emisiones-asociadas?start_date=" + lastYear + "-12-" + currentDay + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=day";
        } else {
            PRODUCTION_DAY_RENEWABLE_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-renovable-no-renovable?start_date=" + currentYear + "-" + lastMonth + "-" + currentDay + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=day";
            PRODUCTION_DAY_EMISSIONS_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-estructura-generacion-emisiones-asociadas?start_date=" + currentYear + "-" + lastMonth + "-" + currentDay + "T00:00&end_date=" + currentDate + "T23:59&time_trunc=day";
        }
        //MONTH
        if (currentMonth.equals("01")) {
            PRODUCTION_MONTH_RENEWABLE_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-renovable-no-renovable?start_date=" + lastYear + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=month";
            PRODUCTION_MONTH_EMISSIONS_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-estructura-generacion-emisiones-asociadas?start_date=" + lastYear + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=month";
        } else {
            PRODUCTION_MONTH_RENEWABLE_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-renovable-no-renovable?start_date=" + lastYear + "-" + currentMonth + "-01" + "T00:00&end_date=" + currentYear + "-" + lastMonth + "-31" + "T23:59&time_trunc=month";
            PRODUCTION_MONTH_EMISSIONS_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-estructura-generacion-emisiones-asociadas?start_date=" + lastYear + "-" + currentMonth + "-01" + "T00:00&end_date=" + currentYear + "-" + lastMonth + "-31" + "T23:59&time_trunc=month";
        }
        //YEAR
        PRODUCTION_YEAR_RENEWABLE_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-renovable-no-renovable?start_date=" + year5Before + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=year";
        PRODUCTION_YEAR_EMISSIONS_PROPORTION_URL = "https://apidatos.ree.es/es/datos/generacion/evolucion-estructura-generacion-emisiones-asociadas?start_date=" + year5Before + "-01-01" + "T00:00&end_date=" + lastYear + "-12-31" + "T23:59&time_trunc=year";


        /*-----AIMACIONES-----*/
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


        /*-----PRIMERA VEZ-----*/
        secondTime = readDataBase();
        if (!secondTime) {
            createDataBase();
        } else {
            /*---CONSUMPTION---*/
            //DAY
            requestDataWriteDataBase(CONSUMPTION_DAY_REALDEMAND_URL, "consumptionDayDemandRealTime.json");
            requestDataWriteDataBase(CONSUMPTION_DAY_DEMANDPERDAY_URL, "consumptionDayDemand.json");
            //MONTH
            requestDataWriteDataBase(CONSUMPTION_MONTH_DEMAND_URL, "consumptionMonthDemand.json");
            requestDataWriteDataBase(CONSUMPTION_MONTH_PRICE_URL, "consumptionMonthPrice.json");
            //YEAR
            requestDataWriteDataBase(CONSUMPTION_YEAR_DEMAND_URL, "consumptionYearDemand.json");
            requestDataWriteDataBase(CONSUMPTION_YEAR_PRICE_URL, "consumptionYearPrice.json");
            /*---PRODUCTION---*/
            //DAY
            requestDataWriteDataBase(PRODUCTION_DAY_GENERATION_STRUCTURE_URL, "productionDayGenerationStructure.json");
            requestDataWriteDataBase(PRODUCTION_DAY_RENEWABLE_PROPORTION_URL, "productionDayRenewableProportion.json");
            requestDataWriteDataBase(PRODUCTION_DAY_EMISSIONS_PROPORTION_URL, "productionDayEmissionsProportion.json");
            //MONTH
            requestDataWriteDataBase(PRODUCTION_MONTH_RENEWABLE_PROPORTION_URL, "productionMonthRenewableProportion.json");
            requestDataWriteDataBase(PRODUCTION_MONTH_EMISSIONS_PROPORTION_URL, "productionMonthEmissionsProportion.json");
            //YEAR
            requestDataWriteDataBase(PRODUCTION_YEAR_RENEWABLE_PROPORTION_URL, "productionYearRenewableProportion.json");
            requestDataWriteDataBase(PRODUCTION_YEAR_EMISSIONS_PROPORTION_URL, "productionYearEmissionsProportion.json");
        }

        /*ABRIR APP*/
        openApp(true);

    }

    private boolean readDataBase() {
        secondTime = new File(getFilesDir() + "/data_base.dat").exists();
        return secondTime;
    }

    private void createDataBase() {
        try {
            //Creacion ficheros iniciales
            new File(getFilesDir() + "/data_base.dat").createNewFile();
            /*-----CONSUMPTION-----*/
            //DAY
            new File(getFilesDir(), "/" + "consumptionDayDemandRealTime.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_DAY_REALDEMAND_URL, "consumptionDayDemandRealTime.json");
            new File(getFilesDir(), "/" + "consumptionDayDemand.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_DAY_DEMANDPERDAY_URL, "consumptionDayDemand.json");
            //MONTH
            new File(getFilesDir(), "/" + "consumptionMonthDemand.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_MONTH_DEMAND_URL, "consumptionMonthDemand.json");
            new File(getFilesDir(), "/" + "consumptionMonthPrice.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_MONTH_PRICE_URL, "consumptionMonthPrice.json");
            //YEAR
            new File(getFilesDir(), "/" + "consumptionYearDemand.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_YEAR_DEMAND_URL, "consumptionYearDemand.json");
            new File(getFilesDir(), "/" + "consumptionYearPrice.json").createNewFile();
            requestDataWriteDataBase(CONSUMPTION_YEAR_PRICE_URL, "consumptionYearPrice.json");
            /*-----PRODUCTION-----*/
            //DAY
            new File(getFilesDir(), "/" + "productionDayGenerationStructure.json").createNewFile();
            requestDataWriteDataBase(PRODUCTION_DAY_GENERATION_STRUCTURE_URL, "productionDayGenerationStructure.json");
            new File(getFilesDir(), "/" + "productionDayRenewableProportion.json").createNewFile();
            requestDataWriteDataBase(PRODUCTION_DAY_RENEWABLE_PROPORTION_URL, "productionDayRenewableProportion.json");
            new File(getFilesDir(), "/" + "productionDayEmissionsProportion.json").createNewFile();
            requestDataWriteDataBase(PRODUCTION_DAY_EMISSIONS_PROPORTION_URL, "productionDayEmissionsProportion.json");
            //MONTH
            new File(getFilesDir(), "/" + "productionMonthRenewableProportion.json").createNewFile();
            requestDataWriteDataBase(PRODUCTION_MONTH_RENEWABLE_PROPORTION_URL, "productionMonthRenewableProportion.json");
            new File(getFilesDir(), "/" + "productionMonthEmissionsProportion.json").createNewFile();
            requestDataWriteDataBase(PRODUCTION_MONTH_EMISSIONS_PROPORTION_URL, "productionMonthEmissionsProportion.json");
            //YEAR
            new File(getFilesDir(), "/" + "productionYearRenewableProportion.json").createNewFile();
            requestDataWriteDataBase(PRODUCTION_YEAR_RENEWABLE_PROPORTION_URL, "productionYearRenewableProportion.json");
            new File(getFilesDir(), "/" + "productionYearEmissionsProportion.json").createNewFile();
            requestDataWriteDataBase(PRODUCTION_YEAR_EMISSIONS_PROPORTION_URL, "productionYearEmissionsProportion.json");

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
        return sdf.format(date);
    }

    private void openApp(boolean locationPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (!secondTime) {
                    intent = new Intent(SplashScreen.this, InfoSlides.class);
                } else {
                    Boolean isConsumptionFavourite = isConsumptionFavourite();

                    if (isConsumptionFavourite) {
                        intent = new Intent(SplashScreen.this, Consumption.class);
                        overridePendingTransition(0, 0);
                    } else {
                        intent = new Intent(SplashScreen.this, Production.class);
                        overridePendingTransition(0, 0);
                    }

                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 2000);
    }

    public boolean isConsumptionFavourite() {
        final boolean ifConsumptionFavourite = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("ifConsumptionFavourite", true);
        return ifConsumptionFavourite;
    }

}