package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

public class Consumption1 extends Fragment {
    private View view;
    private View view2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_consumption1, container, false);
        view2 = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        //ALERT DIALOG
        builder.setView(view2);
        AlertDialog dialog = builder.create();
        dialog.show();

        //LINE CHART
        AnyChartView lineChart = (AnyChartView) view2.findViewById(R.id.anychartView);
        APIlib.getInstance().setActiveAnyChartView(lineChart);
        //lineChart.setProgressBar(view.findViewById(R.id.progress_barGeneracionyConsumo));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        //cartesian.background("#000");
        //cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Demanda en tiempo real");

        cartesian.yAxis(0).title("Demanda (MW)");
        cartesian.xAxis(0).labels().padding(1d, 1d, 1d, 1d);

        //-----------------------------------------------
        //Leer el archivo y crear el objeto JSON
        BufferedReader br = null;
        String json = "";
        JSONObject jsonO1 = new JSONObject();
        try {
            br = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(), "/" + "consumptionDayDemandaTiempoReal.json")));
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
            JSONArray jsonArrayValoresDemandaReal = jsonO1.getJSONArray("included").getJSONObject(0).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayValoresDemandaProgramada = jsonO1.getJSONArray("included").getJSONObject(1).getJSONObject("attributes").getJSONArray("values");
            JSONArray jsonArrayValoresDemandaPrevista = jsonO1.getJSONArray("included").getJSONObject(2).getJSONObject("attributes").getJSONArray("values");

            String hora = "";
            int real = 0;
            int programada = 0;
            int prevista = 0;

            for (int i = 0; i < 144; i += 6) {
                hora = jsonArrayValoresDemandaProgramada.getJSONObject(i).get("datetime").toString().substring(12, 16);
                real = Integer.parseInt(jsonArrayValoresDemandaReal.getJSONObject(i).getString("value"));
                programada = Integer.parseInt(jsonArrayValoresDemandaProgramada.getJSONObject(i).getString("value"));
                prevista = Integer.parseInt(jsonArrayValoresDemandaPrevista.getJSONObject(i).getString("value"));

                seriesData.add(new CustomDataEntry(hora, real, programada, prevista));
            }
            for (DataEntry i :
                    seriesData) {
                System.out.println(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //------------------------------------------------

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

        return view;
    }

    private static class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }

}