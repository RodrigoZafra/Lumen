package com.rotirmar.lumen;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class DataBaseOperations extends AppCompatActivity {

    /*public void createDataBase() {
        try {
            //Creacion ficheros iniciales
            new File(getApplicationContext().getFilesDir() + "/data_base.dat").createNewFile();
            //CONSUMPTION
            //DAY
            new File(getFilesDir(), "/" + "consumptionDayDemanda.json").createNewFile();
            //requestDataWriteDataBase(CONSUMPTION_DAY_URL, "consumptionDayDemanda.json");
            new File(getFilesDir(), "/" + "consumptionMonthDemanda.json").createNewFile();
            new File(getFilesDir(), "/" + "consumptionDayYearDemanda.json").createNewFile();
            //requestDataWriteDataBase(CONSUMPTION_YEAR_URL, "consumptionYearDemanda.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestDataWriteDataBase(String url, String archiveName) {

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

                    Toast.makeText(DataBaseOperations.this, "Database creada", Toast.LENGTH_SHORT).show();

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

                Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
