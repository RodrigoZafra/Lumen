package com.rotirmar.lumen.infoSlidesFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rotirmar.lumen.InfoSlides;
import com.rotirmar.lumen.MainActivity;
import com.rotirmar.lumen.R;

public class InfoSlide1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_slide1, container, false);
    }

}