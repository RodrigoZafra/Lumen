package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

public class Consumption1 extends Fragment {
    private View viewAnyChartPattern;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_consumption1, container, false);
        viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        CardView cvConsumptionDay1 = view.findViewById(R.id.cvConsumptionDay1);
        CardView cvConsumptionDay2 = view.findViewById(R.id.cvConsumptionDay2);

        TextView cvAPITitleConsumptionDay2 = view.findViewById(R.id.cvAPITitleConsumptionDay2);
        cvAPITitleConsumptionDay2.setText(maxValueDemand());

        cvConsumptionDay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnychartRealDemand();
            }
        });

        cvConsumptionDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartDemandPerDay();
            }
        });

        return view;
    }

    private String maxValueDemand() {
        JSONObject jsonObject = readFileAndGenerateJsonObject("consumptionDayDemand.json");
        double value = 1.0D;
        try {
            JSONArray jsonArrayOfDemandPerDayValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            double aux;

            for (int i = 0; i < 31; i++) {
                aux = (Double.parseDouble(jsonArrayOfDemandPerDayValues.getJSONObject(i).getString("value")) / 1000);
                if (aux > value)
                    value = aux;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.format("%.2f", value);
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

    private void generateAnychartRealDemand() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("consumptionDayDemandRealTime.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfRealDemandValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayOfPlannedDemandValues = jsonObject.getJSONArray("included").getJSONObject(1).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayOfExpectedDemandValues = jsonObject.getJSONArray("included").getJSONObject(2).getJSONObject("attributes").getJSONArray("values");

            String hour;
            int real;
            int planned;
            int expected;

            for (int i = 0; i < 144; i += 6) {
                hour = jsonArrayOfPlannedDemandValues.getJSONObject(i).get("datetime").toString().substring(12, 16);
                try {
                    real = Integer.parseInt(jsonArrayOfRealDemandValues.getJSONObject(i).getString("value"));
                } catch (JSONException e) {
                    real = 0;
                }
                planned = Integer.parseInt(jsonArrayOfPlannedDemandValues.getJSONObject(i).getString("value"));
                expected = Integer.parseInt(jsonArrayOfExpectedDemandValues.getJSONObject(i).getString("value"));

                seriesData.add(new customDataEntryRealTimeDemand(hour, real, planned, expected));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //------------------------------------------------


        //-----------------------GENERATE LINE CHART (DEMAND REAL TIME)------------------------
        AnyChartView lineChart = (AnyChartView) viewAnyChartPattern.findViewById(R.id.anychartView);
        APIlib.getInstance().setActiveAnyChartView(lineChart);
        lineChart.setZoomEnabled(true);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        //cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Demanda en tiempo real");

        cartesian.yAxis(0).title("MW");
        cartesian.xAxis(0).labels().padding(1d, 1d, 1d, 1d);

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Real");
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
        series2.name("Programada");
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

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Prevista");
        series3.color("#41d641");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        lineChart.setChart(cartesian);
    }

    private void generateAnyChartDemandPerDay() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("consumptionDayDemand.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfDemandPerDayValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            String day;
            double value;

            for (int i = 0; i < 31; i++) {
                day = jsonArrayOfDemandPerDayValues.getJSONObject(i).get("datetime").toString().substring(5, 10);
                value = (Double.parseDouble(jsonArrayOfDemandPerDayValues.getJSONObject(i).getString("value")) / 1000);

                seriesData.add(new customDataEntryDemandPerDay(day, value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //------------------------------------------------


        //-----------------------GENERATE COLUM CHART (DEMAND PER DAY)------------------------
        //FULL COLUMNAS, HABRÁ QUE RENOMBRAR EL GRÁFICO y LA PROGRESS
        AnyChartView anyChartView = viewAnyChartPattern.findViewById(R.id.anychartView);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
        anyChartView.setZoomEnabled(true);

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
        cartesian.title("Demanda por dia del último mes");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.yAxis(0).title("GWh");
        //cartesian.xAxis(0).title("Día");

        anyChartView.setChart(cartesian);
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

    private static class customDataEntryRealTimeDemand extends ValueDataEntry {

        customDataEntryRealTimeDemand(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }

    private static class customDataEntryDemandPerDay extends ValueDataEntry {

        customDataEntryDemandPerDay(String x, Number value) {
            super(x, value);
        }

    }

}