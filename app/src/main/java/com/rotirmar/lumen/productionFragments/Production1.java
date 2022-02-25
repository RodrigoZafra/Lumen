package com.rotirmar.lumen.productionFragments;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
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

public class Production1 extends Fragment {
    private View viewAnyChartPattern;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_production1, container, false);
        viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        CardView cvProductionDay1 = view.findViewById(R.id.cvProductionDay1);
        CardView cvProductionDay2 = view.findViewById(R.id.cvProductionDay2);
        CardView cvProductionDay3 = view.findViewById(R.id.cvProductionDay3);

        TextView cvAPITitleProductionDay1 = view.findViewById(R.id.cvAPITitleProductionDay1);
        cvAPITitleProductionDay1.setText(maxValue("productionDayGenerationStructure.json"));
        TextView cvAPITitleProductionDay2 = view.findViewById(R.id.cvAPITitleProductionDay2);
        cvAPITitleProductionDay2.setText(maxValue("productionDayRenewableProportion.json"));
        TextView cvAPITitleProductionDay3 = view.findViewById(R.id.cvAPITitleProductionDay3);
        cvAPITitleProductionDay3.setText(maxValue("productionDayEmissionsProportion.json"));

        cvProductionDay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartDayProductionStructure();
            }
        });

        cvProductionDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartDayRenewableNoRenewable();
            }
        });

        cvProductionDay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartDayEmissionsNoEmissions();
            }
        });

        return view;
    }

    private String maxValue(String file) {
        JSONObject jsonObject = readFileAndGenerateJsonObject(file);
        String chain = "";
        try {
            if (file.equals("productionDayGenerationStructure.json")) {
                JSONArray jsonArrayOfProductionStructure = jsonObject.getJSONArray("included");

                double value = 0.0;
                double aux;

                for (int i = 0; i < 16; i++) {
                    aux = (Double.parseDouble(jsonArrayOfProductionStructure.getJSONObject(i).getJSONObject("attributes").getJSONArray("values").getJSONObject(0).getString("value")) / 1000);
                    if (aux > value) {
                        value = aux;
                        chain = jsonArrayOfProductionStructure.getJSONObject(i).getString("type");
                    }
                }
            } else {
                JSONArray jsonArrayOfRenewableValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
                JSONArray jsonArrayOfNoRenewableValues = jsonObject.getJSONArray("included").getJSONObject(1).getJSONObject("attributes").getJSONArray("values");


                double renewableMax = 0.0;
                double renewable;
                double noRenewable;
                double sum;

                for (int i = 0; i < 32; i++) {
                    renewable = (Double.parseDouble(jsonArrayOfRenewableValues.getJSONObject(i).getString("value")) / 1000);
                    noRenewable = (Double.parseDouble(jsonArrayOfNoRenewableValues.getJSONObject(i).getString("value")) / 1000);
                    sum = renewable + noRenewable;
                    renewable = Math.round((renewable * 100) / sum);

                    if (renewable > renewableMax){
                        renewableMax = renewable;
                    }
                }
                chain = renewableMax + "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chain;
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

    private void generateAnyChartDayProductionStructure() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("productionDayGenerationStructure.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfProductionStructure = jsonObject.getJSONArray("included");

            String technology;
            double value;

            for (int i = 0; i < 16; i++) {
                technology = jsonArrayOfProductionStructure.getJSONObject(i).getString("type");
                value = (Double.parseDouble(jsonArrayOfProductionStructure.getJSONObject(i).getJSONObject("attributes").getJSONArray("values").getJSONObject(0).getString("value")) / 1000);

                seriesData.add(new customDataEntry(technology, value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //------------------------------------------------


        //-----------------------GENERATE COLUM CHART (DAY PRODUCTION STRUCTURE)------------------------
        //FULL COLUMNAS, HABRÁ QUE RENOMBRAR EL GRÁFICO y LA PROGRESS
        AnyChartView anyChartView = viewAnyChartPattern.findViewById(R.id.anychartView);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        Column column = cartesian.column(seriesData);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: } GWh");

        cartesian.animation(true);
        cartesian.title(getString(R.string.string_consumption_chart7_title));

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.yAxis(0).title("GWh");
        //cartesian.xAxis(0).title("Día");

        anyChartView.setChart(cartesian);
    }

    private void generateAnyChartDayRenewableNoRenewable() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("productionDayRenewableProportion.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfRenewableValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayOfNoRenewableValues = jsonObject.getJSONArray("included").getJSONObject(1).getJSONObject("attributes").getJSONArray("values");

            String day;
            double renewable;
            double noRenewable;
            double sum;

            for (int i = 0; i < 32; i++) {
                day = jsonArrayOfNoRenewableValues.getJSONObject(i).get("datetime").toString().substring(6, 10);
                renewable = (Double.parseDouble(jsonArrayOfRenewableValues.getJSONObject(i).getString("value")) / 1000);
                noRenewable = (Double.parseDouble(jsonArrayOfNoRenewableValues.getJSONObject(i).getString("value")) / 1000);
                sum = renewable + noRenewable;

                renewable = Math.round((renewable * 100) / sum);
                noRenewable = Math.round((noRenewable * 100) / sum);

                seriesData.add(new customDataEntryLinear(day, renewable, noRenewable));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //-----------------------GENERATE LINE CHART (RENEWABLE/NO RENEWABLE)------------------------
        generateLineChart(seriesData, getString(R.string.string_consumption_chart8_title), "%", getString(R.string.string_consumption_chart8_s1), getString(R.string.string_consumption_chart8_s2));
    }

    private void generateAnyChartDayEmissionsNoEmissions() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("productionDayEmissionsProportion.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfRenewableValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayOfNoRenewableValues = jsonObject.getJSONArray("included").getJSONObject(1).getJSONObject("attributes").getJSONArray("values");

            String day;
            double emissions;
            double noEmissions;
            double sum;

            for (int i = 0; i < 32; i++) {
                day = jsonArrayOfNoRenewableValues.getJSONObject(i).get("datetime").toString().substring(6, 10);
                emissions = (Double.parseDouble(jsonArrayOfRenewableValues.getJSONObject(i).getString("value")) / 1000);
                noEmissions = (Double.parseDouble(jsonArrayOfNoRenewableValues.getJSONObject(i).getString("value")) / 1000);
                sum = emissions + noEmissions;

                emissions = Math.round((emissions * 100) / sum);
                noEmissions = Math.round((noEmissions * 100) / sum);

                seriesData.add(new customDataEntryLinear(day, emissions, noEmissions));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //-----------------------GENERATE LINE CHART (RENEWABLE/NO RENEWABLE)------------------------
        generateLineChart(seriesData, getString(R.string.string_consumption_chart9_title), "%", getString(R.string.string_consumption_chart9_s1), getString(R.string.string_consumption_chart9_s2));
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

    private static class customDataEntry extends ValueDataEntry {
        customDataEntry(String x, Number value) {
            super(x, value);
        }
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