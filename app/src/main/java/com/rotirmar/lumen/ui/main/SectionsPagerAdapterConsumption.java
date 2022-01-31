package com.rotirmar.lumen.ui.main;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rotirmar.lumen.consumptionFragments.Consumption1;
import com.rotirmar.lumen.consumptionFragments.Consumption2;
import com.rotirmar.lumen.consumptionFragments.Consumption3;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterConsumption extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapterConsumption(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Consumption1();
            case 1:
                return new Consumption2();
            case 2:
                return new Consumption3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}