package com.rotirmar.lumen.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rotirmar.lumen.R;
import com.rotirmar.lumen.consumptionFragments.Consumption1;
import com.rotirmar.lumen.consumptionFragments.Consumption2;
import com.rotirmar.lumen.consumptionFragments.Consumption3;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterProduction extends FragmentPagerAdapter {

    private final Context mContext;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.consumption_tab1,
            R.string.consumption_tab2,
            R.string.consumption_tab3
    };

    public SectionsPagerAdapterProduction(Context context, FragmentManager fm) {
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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}