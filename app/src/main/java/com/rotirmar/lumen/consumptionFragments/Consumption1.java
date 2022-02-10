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

        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.prueba);
        Pie pie = AnyChart.pie();

        List<DataEntry> datat = new ArrayList<>();
        datat.add(new ValueDataEntry("John", 10000));
        datat.add(new ValueDataEntry("Jake", 12000));
        datat.add(new ValueDataEntry("Peter", 18000));

        pie.data(datat);

        anyChartView.setChart(pie);

        return view;
    }
}