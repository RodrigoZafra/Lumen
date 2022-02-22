package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

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
import com.anychart.charts.Funnel;
import com.anychart.charts.Pie;
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
import com.rotirmar.lumen.R;

import java.util.ArrayList;
import java.util.List;

public class Consumption1 extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consumption1, container, false);

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

        anyChartView.setChart(cartesian);
        return view;

        //AnyChartView pieChart = (AnyChartView) view.findViewById(R.id.graficoGeneracionPorcentual);
        //APIlib.getInstance().setActiveAnyChartView(pieChart);
        //Pie pie = AnyChart.pie();
//
        //List<DataEntry> datat = new ArrayList<>();
        //datat.add(new ValueDataEntry("Hidráulica", 11.1));
        //datat.add(new ValueDataEntry("Eólica", 30));
        //datat.add(new ValueDataEntry("Solar fotovoltaica", 4.4));
        //datat.add(new ValueDataEntry("Solar térmica", 0.5));
        //datat.add(new ValueDataEntry("Otras renovables", 1.9));
        //datat.add(new ValueDataEntry("Residuos renovables", 0.3));
        //datat.add(new ValueDataEntry("Nuclear", 17.3));
        //datat.add(new ValueDataEntry("Turbinación bombeo", 1.2));
        //datat.add(new ValueDataEntry("Ciclo combinado", 19.8));
        //datat.add(new ValueDataEntry("Carbón", 3.2));
        //datat.add(new ValueDataEntry("Cogeneración", 9.6));
        //datat.add(new ValueDataEntry("Residuos no renovables", 0.8));
//
        //pie.data(datat);
//
        //pieChart.setChart(pie);
//
        //AnyChartView lineChart = (AnyChartView) view.findViewById(R.id.graficoGeneracionyConsumo);
        //APIlib.getInstance().setActiveAnyChartView(lineChart);
        ////lineChart.setProgressBar(view.findViewById(R.id.progress_barGeneracionyConsumo));
//
        //Cartesian cartesian = AnyChart.line();
//
        //cartesian.animation(true);
//
        //cartesian.padding(10d, 20d, 5d, 20d);
//
        //cartesian.crosshair().enabled(true);
        //cartesian.crosshair()
        //        .yLabel(true)
        //        // TODO ystroke
        //        .yStroke((Stroke) null, null, null, (String) null, (String) null);
//
        //cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//
        //cartesian.title("Demanda y generación energética.");
//
        //cartesian.yAxis(0).title("Demanda (MW)");
        //cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
//
        //List<DataEntry> seriesData = new ArrayList<>();
        //seriesData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));
        //seriesData.add(new CustomDataEntry("1987", 7.1, 4.0, 4.1));
        //seriesData.add(new CustomDataEntry("1988", 8.5, 6.2, 5.1));
        //seriesData.add(new CustomDataEntry("1989", 9.2, 11.8, 6.5));
        //seriesData.add(new CustomDataEntry("1990", 10.1, 13.0, 12.5));
        //seriesData.add(new CustomDataEntry("1991", 11.6, 13.9, 18.0));
        //seriesData.add(new CustomDataEntry("1992", 16.4, 18.0, 21.0));
        //seriesData.add(new CustomDataEntry("1993", 18.0, 23.3, 20.3));
        //seriesData.add(new CustomDataEntry("1994", 13.2, 24.7, 19.2));
        //seriesData.add(new CustomDataEntry("1995", 12.0, 18.0, 14.4));
        //seriesData.add(new CustomDataEntry("1996", 3.2, 15.1, 9.2));
        //seriesData.add(new CustomDataEntry("1997", 4.1, 11.3, 5.9));
        //seriesData.add(new CustomDataEntry("1998", 6.3, 14.2, 5.2));
        //seriesData.add(new CustomDataEntry("1999", 9.4, 13.7, 4.7));
        //seriesData.add(new CustomDataEntry("2000", 11.5, 9.9, 4.2));
        //seriesData.add(new CustomDataEntry("2001", 13.5, 12.1, 1.2));
        //seriesData.add(new CustomDataEntry("2002", 14.8, 13.5, 5.4));
        //seriesData.add(new CustomDataEntry("2003", 16.6, 15.1, 6.3));
        //seriesData.add(new CustomDataEntry("2004", 18.1, 17.9, 8.9));
        //seriesData.add(new CustomDataEntry("2005", 17.0, 18.9, 10.1));
        //seriesData.add(new CustomDataEntry("2006", 16.6, 20.3, 11.5));
        //seriesData.add(new CustomDataEntry("2007", 14.1, 20.7, 12.2));
        //seriesData.add(new CustomDataEntry("2008", 15.7, 21.6, 10));
        //seriesData.add(new CustomDataEntry("2009", 12.0, 22.5, 8.9));
//
        //Set set = Set.instantiate();
        //set.data(seriesData);
        //Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        //Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        //Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
//
        //Line series1 = cartesian.line(series1Mapping);
        //series1.name("Prevista");
        //series1.hovered().markers().enabled(true);
        //series1.hovered().markers()
        //        .type(MarkerType.CIRCLE)
        //        .size(4d);
        //series1.tooltip()
        //        .position("right")
        //        .anchor(Anchor.LEFT_CENTER)
        //        .offsetX(5d)
        //        .offsetY(5d);
//
        //Line series2 = cartesian.line(series2Mapping);
        //series2.name("Prevista");
        //series2.hovered().markers().enabled(true);
        //series2.hovered().markers()
        //        .type(MarkerType.CIRCLE)
        //        .size(4d);
        //series2.tooltip()
        //        .position("right")
        //        .anchor(Anchor.LEFT_CENTER)
        //        .offsetX(5d)
        //        .offsetY(5d);
//
        //Line series3 = cartesian.line(series3Mapping);
        //series3.name("Real");
        //series3.hovered().markers().enabled(true);
        //series3.hovered().markers()
        //        .type(MarkerType.CIRCLE)
        //        .size(4d);
        //series3.tooltip()
        //        .position("right")
        //        .anchor(Anchor.LEFT_CENTER)
        //        .offsetX(5d)
        //        .offsetY(5d);
//
        //cartesian.legend().enabled(true);
        //cartesian.legend().fontSize(13d);
        //cartesian.legend().padding(0d, 0d, 10d, 0d);
//
        //lineChart.setChart(cartesian);
        //return view;

    }
}