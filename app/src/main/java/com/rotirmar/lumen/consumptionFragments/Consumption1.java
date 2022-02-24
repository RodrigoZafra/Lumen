package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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
    private View viewAnyChartPattern;

    private CardView cvConsumptionDay1;
    private CardView cvConsumptionDay2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_consumption1, container, false);
        viewAnyChartPattern = inflater.inflate(R.layout.alert_dialog_pattern, container, false);

        cvConsumptionDay1 = view.findViewById(R.id.cvConsumptionDay1);
        cvConsumptionDay2 = view.findViewById(R.id.cvConsumptionDay2);

        //generarAnychartConsumoReal();

        cvConsumptionDay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ALERT DIALOG
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setView(viewAnyChartPattern);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        cvConsumptionDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*/FULL COLUMNAS, HABRÁ QUE RENOMBRAR EL GRÁFICO y LA PROGRESS

        AnyChartView anyChartView = view.findViewById(R.id.graficoColumnas);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Rouge", 80540));
        data.add(new ValueDataEntry("Foundation", 94190));
        data.add(new ValueDataEntry("Mascara", 102610));
        data.add(new ValueDataEntry("Lip gloss", 110430));
        data.add(new ValueDataEntry("Lipstick", 128000));
        data.add(new ValueDataEntry("Nail polish", 143760));
        data.add(new ValueDataEntry("Eyebrow pencil", 170670));
        data.add(new ValueDataEntry("Eyeliner", 213210));
        data.add(new ValueDataEntry("Eyeshadows", 249980));

        Column column = cartesian.column(data);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Top 10 Cosmetic Products by Revenue");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Revenue");

        anyChartView.setChart(cartesian);*/

        return view;
    }

    private static class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }

    private void generarAnychartConsumoReal() {
        AnyChartView lineChart = (AnyChartView) viewAnyChartPattern.findViewById(R.id.any_chart_view);
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
    }

}