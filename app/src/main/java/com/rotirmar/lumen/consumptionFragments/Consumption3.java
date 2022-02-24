package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rotirmar.lumen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Consumption3 extends Fragment {
    private View view;
    private View viewAnyChartPattern;

    private CardView cvConsumptionYear1;
    private CardView cvConsumptionYear2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consumption3, container, false);
        viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        cvConsumptionYear1 = view.findViewById(R.id.cvConsumptionYear1);
        cvConsumptionYear2 = view.findViewById(R.id.cvConsumptionYear2);


        cvConsumptionYear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewAnyChartPattern != null) {
                    ViewGroup parent = (ViewGroup) viewAnyChartPattern.getParent();
                    if (parent != null) {
                        parent.removeView(viewAnyChartPattern);
                    }
                }
                try {
                    viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);
                } catch (InflateException e) {

                }

                generarAnychartDemandaPorYear();
                //ALERT DIALOG
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setView(viewAnyChartPattern);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        cvConsumptionYear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewAnyChartPattern != null) {
                    ViewGroup parent = (ViewGroup) viewAnyChartPattern.getParent();
                    if (parent != null) {
                        parent.removeView(viewAnyChartPattern);
                    }
                }
                try {
                    viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);
                } catch (InflateException e) {

                }

                generarAnychartPrecioPorYear();
                //ALERT DIALOG
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setView(viewAnyChartPattern);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return view;
    }

    private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }

    private void generarAnychartDemandaPorYear() {
        AnyChartView anyChartView = viewAnyChartPattern.findViewById(R.id.anychartView);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        //-----------------------------------------------
        //Leer el archivo y crear el objeto JSON
        BufferedReader br;
        String json = "";
        JSONObject jsonO1 = new JSONObject();
        try {
            br = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(), "/" + "consumptionYearDemanda.json")));
            json = br.readLine();
            br.close();
            jsonO1 = new JSONObject(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayValoresDemandaPorYear = jsonO1.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            String year;
            Double value;

            for (int i = 0; i < 5; i++) {
                year = jsonArrayValoresDemandaPorYear.getJSONObject(i).get("datetime").toString().substring(0, 4);
                value = Double.parseDouble(jsonArrayValoresDemandaPorYear.getJSONObject(i).getString("value"));

                seriesData.add(new CustomDataEntry(year, value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //------------------------------------------------

        Column column = cartesian.column(seriesData);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: } MW/h");

        cartesian.animation(true);
        cartesian.title("Demanda por año");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: } MW/h");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Día");
        cartesian.yAxis(0).title("MW");

        anyChartView.setChart(cartesian);
    }

    private void generarAnychartPrecioPorYear() {
        AnyChartView anyChartView = viewAnyChartPattern.findViewById(R.id.anychartView);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        //-----------------------------------------------
        //Leer el archivo y crear el objeto JSON
        BufferedReader br;
        String json = "";
        JSONObject jsonO1 = new JSONObject();
        try {
            br = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(), "/" + "consumptionYearPrice.json")));
            json = br.readLine();
            br.close();
            jsonO1 = new JSONObject(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayValoresPricePorYear = jsonO1.getJSONArray("included").getJSONObject(3).getJSONObject("attributes").getJSONArray("content").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            String year;
            Double value;

            for (int i = 0; i < 5; i++) {
                year = jsonArrayValoresPricePorYear.getJSONObject(i).get("datetime").toString().substring(0, 4);
                value = Double.parseDouble(jsonArrayValoresPricePorYear.getJSONObject(i).getString("value"));

                seriesData.add(new CustomDataEntry(year, value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //------------------------------------------------

        Column column = cartesian.column(seriesData);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: } €");

        cartesian.animation(true);
        cartesian.title("Precio medio por año");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: } €");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Día");
        cartesian.yAxis(0).title("MW");

        anyChartView.setChart(cartesian);
    }
}