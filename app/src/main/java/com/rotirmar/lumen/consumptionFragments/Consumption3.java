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
    private View viewAnyChartPattern;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumption3, container, false);
        viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        CardView cvConsumptionYear1 = view.findViewById(R.id.cvConsumptionYear1);
        CardView cvConsumptionYear2 = view.findViewById(R.id.cvConsumptionYear2);

        TextView cvAPITitleConsumptionYear1 = view.findViewById(R.id.cvAPITitleConsumptionYear1);
        cvAPITitleConsumptionYear1.setText(maxValue("consumptionYearDemand.json", 1000));
        TextView cvAPITitleConsumptionYear2 = view.findViewById(R.id.cvAPITitleConsumptionYear2);
        cvAPITitleConsumptionYear2.setText(maxValue("consumptionYearPrice.json", 1));

        cvConsumptionYear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartDemandPerYear();
            }
        });

        cvConsumptionYear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanViewAnyChartPattern(inflater, container);
                generateAlertDialog();
                generateAnyChartPricePerYear();
            }
        });

        return view;
    }

    private String maxValue(String file, int divisor) {
        JSONObject jsonObject = readFileAndGenerateJsonObject(file);
        double value = 1.0D;
        try {
            JSONArray jsonArrayValues;
            if (divisor == 1000)
                jsonArrayValues = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
            else
                jsonArrayValues = jsonObject.getJSONArray("included").getJSONObject(3).getJSONObject("attributes").getJSONArray("content").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            double aux;

            for (int i = 0; i < 5; i++) {
                aux = (Double.parseDouble(jsonArrayValues.getJSONObject(i).getString("value")) / divisor);
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

    private void generateAnyChartDemandPerYear() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("consumptionYearDemand.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfDemandValuesPerYear = jsonObject.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            String year;
            double value;

            for (int i = 0; i < 5; i++) {
                year = jsonArrayOfDemandValuesPerYear.getJSONObject(i).get("datetime").toString().substring(0, 4);
                value = (Double.parseDouble(jsonArrayOfDemandValuesPerYear.getJSONObject(i).getString("value")) / 1000);

                seriesData.add(new CustomDataEntry(year, value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //-----------------------GENERATE COLUM CHART (DEMAND PER YEAR)------------------------
        generateColumChart(seriesData, "GWh", getString(R.string.string_consumption_chart5_title), "GWh");
    }

    private void generateAnyChartPricePerYear() {
        //-----------------------EXTRACT DATA------------------------
        JSONObject jsonObject = readFileAndGenerateJsonObject("consumptionYearPrice.json");

        List<DataEntry> seriesData = new ArrayList<>();

        try {
            JSONArray jsonArrayOfPricePerYear = jsonObject.getJSONArray("included").getJSONObject(3).getJSONObject("attributes").getJSONArray("content").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");

            String year;
            double value;

            for (int i = 0; i < 5; i++) {
                year = jsonArrayOfPricePerYear.getJSONObject(i).get("datetime").toString().substring(0, 4);
                value = Double.parseDouble(jsonArrayOfPricePerYear.getJSONObject(i).getString("value"));

                seriesData.add(new CustomDataEntry(year, value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //-----------------------GENERATE COLUM CHART (PRICE PER YEAR)------------------------
        generateColumChart(seriesData, "€", getString(R.string.string_consumption_chart6_title), "EUROS");
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

    private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }

    private void generateColumChart(List<DataEntry> seriesData, String columFormat, String chartTitle, String yAxisTitle) {
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
                .format("{%Value}{groupsSeparator: } " + columFormat);

        cartesian.animation(true);
        cartesian.title(chartTitle);

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.yAxis(0).title(yAxisTitle);
        //cartesian.xAxis(0).title("Dia");

        anyChartView.setChart(cartesian);
    }
}