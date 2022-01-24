package com.rotirmar.lumen.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rotirmar.lumen.R;
import com.rotirmar.lumen.infoSlidesFragments.InfoSlide1;
import com.rotirmar.lumen.infoSlidesFragments.InfoSlide2;
import com.rotirmar.lumen.infoSlidesFragments.InfoSlide3;
import com.rotirmar.lumen.infoSlidesFragments.InfoSlide4;
import com.rotirmar.lumen.infoSlidesFragments.InfoSlide5;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InfoSlide1();
            case 1:
                return new InfoSlide2();
            case 2:
                return new InfoSlide3();
            case 3:
                return new InfoSlide4();
            case 4:
                return new InfoSlide5();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}