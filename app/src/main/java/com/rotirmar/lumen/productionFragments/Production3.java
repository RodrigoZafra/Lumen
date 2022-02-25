package com.rotirmar.lumen.productionFragments;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
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

public class Production3 extends Fragment {
    private View viewAnyChartPattern;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_production3, container, false);
        viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        CardView cvProductionYear1 = view.findViewById(R.id.cvProductionYear1);
        CardView cvProductionYear2 = view.findViewById(R.id.cvProductionYear2);

        cvProductionYear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartYearRenewableNoRenewable();
            }
        });

        cvProductionYear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartYearEmissionsNoEmissions();
            }
        });

        return view;
    }

    private void cleanViewAnyChartPattern(LayoutInflater inflater, ViewGroup container) {
        //VIEW CLEANER
        if (viewAnyChartPattern != null) {
            ViewGroup parent = (ViewGroup) viewAnyChartPattern.getParent();
            if (parent != null) {
                parent.removeView(viewAnyChartPattern);
            }
        }
        try {
            viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
    }

    private void generateAlertDialog() {
        //ALERT DIALOG GENERATOR
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setView(viewAnyChartPattern);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void generateAnyChartYearRenewableNoRenewable() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("productionYearRenewableProportion.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfRenewableValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayOfNoRenewableValues = jsonObject.getJSONArray("included").getJSONObject(1).getJSONObject("attributes").getJSONArray("values");

            String year;
            double renewable;
            double noRenewable;
            double sum;

            for (int i = 0; i < 5; i++) {
                year = jsonArrayOfNoRenewableValues.getJSONObject(i).get("datetime").toString().substring(0, 4);
                renewable = Double.parseDouble(jsonArrayOfRenewableValues.getJSONObject(i).getString("value"));
                noRenewable = Double.parseDouble(jsonArrayOfNoRenewableValues.getJSONObject(i).getString("value"));
                sum = renewable + noRenewable;

                renewable = Math.round((renewable * 100) / sum);
                noRenewable = Math.round((noRenewable * 100) / sum);

                seriesData.add(new customDataEntryLinear(year, renewable, noRenewable));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //-----------------------GENERATE LINE CHART (RENEWABLE/NO RENEWABLE)------------------------
        generateLineChart(seriesData, "Producción renovable/no renovable", "%", "Renovable", "No renovable");
    }

    private void generateAnyChartYearEmissionsNoEmissions() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("productionYearEmissionsProportion.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfRenewableValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayOfNoRenewableValues = jsonObject.getJSONArray("included").getJSONObject(1).getJSONObject("attributes").getJSONArray("values");

            String day;
            double emissions;
            double noEmissions;
            double sum;

            for (int i = 0; i < 5; i++) {
                day = jsonArrayOfNoRenewableValues.getJSONObject(i).get("datetime").toString().substring(0, 4);
                emissions = (Double.parseDouble(jsonArrayOfRenewableValues.getJSONObject(i).getString("value"))/1000);
                noEmissions = (Double.parseDouble(jsonArrayOfNoRenewableValues.getJSONObject(i).getString("value"))/1000);
                sum = emissions + noEmissions;

                emissions = Math.round((emissions * 100) / sum);
                noEmissions = Math.round((noEmissions * 100) / sum);

                seriesData.add(new customDataEntryLinear(day, emissions, noEmissions));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //-----------------------GENERATE LINE CHART (RENEWABLE/NO RENEWABLE)------------------------
        generateLineChart(seriesData, "Producción con/sin emisiones", "%", "Sin emisiones", "Con emisiones");
    }

    private JSONObject readFileAndGenerateJsonObject(String file) {
        //READ THE FILE AND GENERATE JSON OBJECT
        BufferedReader br;
        String jsonText;
        JSONObject jsonObject = new JSONObject();
        try {
            br = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(), "/" + file)));
            jsonText = br.readLine();
            br.close();
            jsonObject = new JSONObject(jsonText);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static class customDataEntryLinear extends ValueDataEntry {
        customDataEntryLinear(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

    private void generateLineChart(List<DataEntry> seriesData, String chartTitle, String yAxisTitle, String s1Name, String s2Name) {
        AnyChartView lineChart = (AnyChartView) viewAnyChartPattern.findViewById(R.id.anychartView);
        APIlib.getInstance().setActiveAnyChartView(lineChart);
        lineChart.setZoomEnabled(true);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        //cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
                .format("{%Value}{groupsSeparator: } %");

        cartesian.title(chartTitle);

        cartesian.yAxis(0).title(yAxisTitle);
        cartesian.xAxis(0).labels().padding(1d, 1d, 1d, 1d);

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name(s1Name);
        series1.color("#ffea00");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name(s2Name);
        series2.color("#e90b0b");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        lineChart.setChart(cartesian);
    }
}