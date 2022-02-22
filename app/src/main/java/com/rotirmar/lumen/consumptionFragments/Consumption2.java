package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rotirmar.lumen.R;


public class Consumption2 extends Fragment {
    private View view;
    private CardView cvConsumptionMonth1;
    private CardView cvConsumptionMonth2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consumption2, container, false);
        //AQUI LOS NUEVOS VIEWS

        cvConsumptionMonth1 = view.findViewById(R.id.cvConsumptionMonth1);
        cvConsumptionMonth2 = view.findViewById(R.id.cvConsumptionMonth2);

        cvConsumptionMonth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvConsumptionMonth2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //AQUI EL CÓDIGO DE LOS CHARTS
        return view;
    }
}