package com.rotirmar.lumen.consumptionFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rotirmar.lumen.R;

public class Consumption3 extends Fragment {
    private View view;
    private CardView cvConsumptionYear1;
    private CardView cvConsumptionYear2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consumption3, container, false);
        //AQUI LOS NUEVOS VIEWS
        cvConsumptionYear1 = view.findViewById(R.id.cvConsumptionYear1);
        cvConsumptionYear2 = view.findViewById(R.id.cvConsumptionYear2);

        cvConsumptionYear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvConsumptionYear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //AQUI EL CÓDIGO DE LOS CHARTS

        return view;
    }
}