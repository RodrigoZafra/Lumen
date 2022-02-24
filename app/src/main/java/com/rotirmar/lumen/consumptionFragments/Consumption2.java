package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class Consumption2 extends Fragment {
    private View view;
    private View viewAnyChartPattern;

    private CardView cvConsumptionMonth1;
    private CardView cvConsumptionMonth2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consumption2, container, false);
        viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        cvConsumptionMonth1 = view.findViewById(R.id.cvConsumptionMonth1);
        cvConsumptionMonth2 = view.findViewById(R.id.cvConsumptionMonth2);

        cvConsumptionMonth1.setOnClickListener(new View.OnClickListener() {
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

                generarAnychartDemandaPorMes();
                //ALERT DIALOG
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setView(viewAnyChartPattern);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        cvConsumptionMonth2.setOnClickListener(new View.OnClickListener() {
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

                generarAnychartPrecioPorMes();
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

    private void generarAnychartDemandaPorMes() {
        AnyChartView anyChartView = viewAnyChartPattern.findViewById(R.id.anychartView);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        //-----------------------------------------------
        //Leer el archivo y crear el objeto JSON
        BufferedReader br;
        String json = "";
        JSONObject jsonO1 = new JSONObject();
        try {
            br = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(), "/" + "consumptionMonthDemanda.json")));
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
            JSONArray jsonArrayValoresDemandaPorMes = jsonO1.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            String day;
            Double value;

            for (int i = 0; i < 12; i++) {
                day = jsonArrayValoresDemandaPorMes.getJSONObject(i).get("datetime").toString().substring(5, 7);
                value = Double.parseDouble(jsonArrayValoresDemandaPorMes.getJSONObject(i).getString("value"));

                seriesData.add(new CustomDataEntry(day, value));
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
        cartesian.title("Demanda por día del último mes");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: } MW/h");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Día");
        cartesian.yAxis(0).title("MW");

        anyChartView.setChart(cartesian);
    }

    private void generarAnychartPrecioPorMes() {
        AnyChartView anyChartView = viewAnyChartPattern.findViewById(R.id.anychartView);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        //-----------------------------------------------
        //Leer el archivo y crear el objeto JSON
        BufferedReader br;
        String json = "";
        JSONObject jsonO1 = new JSONObject();
        try {
            br = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(), "/" + "consumptionMonthPrice.json")));
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
            JSONArray jsonArrayValoresDemandaPorMes = jsonO1.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            String day;
            Double value;

            for (int i = 0; i < 12; i++) {
                day = jsonArrayValoresDemandaPorMes.getJSONObject(i).get("datetime").toString().substring(5, 7);
                value = Double.parseDouble(jsonArrayValoresDemandaPorMes.getJSONObject(i).getString("value"));

                seriesData.add(new CustomDataEntry(day, value));
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
        cartesian.title("Demanda por día del último mes");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: } MW/h");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Día");
        cartesian.yAxis(0).title("MW");

        anyChartView.setChart(cartesian);
    }
}