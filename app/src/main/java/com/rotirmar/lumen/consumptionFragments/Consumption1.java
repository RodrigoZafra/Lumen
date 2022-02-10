package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Funnel;
import com.anychart.charts.Pie;
import com.rotirmar.lumen.R;

import java.util.ArrayList;
import java.util.List;

public class Consumption1 extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consumption1, container, false);

        AnyChartView pieChart = (AnyChartView) view.findViewById(R.id.graficoGeneracionPorcentual);
        Pie pie = AnyChart.pie();

        List<DataEntry> datat = new ArrayList<>();
        datat.add(new ValueDataEntry("Hidráulica", 11.1));
        datat.add(new ValueDataEntry("Eólica", 30));
        datat.add(new ValueDataEntry("Solar fotovoltaica", 4.4));
        datat.add(new ValueDataEntry("Solar térmica", 0.5));
        datat.add(new ValueDataEntry("Otras renovables", 1.9));
        datat.add(new ValueDataEntry("Residuos renovables", 0.3));
        datat.add(new ValueDataEntry("Nuclear", 17.3));
        datat.add(new ValueDataEntry("Turbinación bombeo", 1.2));
        datat.add(new ValueDataEntry("Ciclo combinado", 19.8));
        datat.add(new ValueDataEntry("Carbón", 3.2));
        datat.add(new ValueDataEntry("Cogeneración", 9.6));
        datat.add(new ValueDataEntry("Residuos no renovables", 0.8));

        pie.data(datat);

        pieChart.setChart(pie);

        AnyChartView lineChart = (AnyChartView) view.findViewById(R.id.graficoGeneracionyConsumo);

        return view;
    }
}