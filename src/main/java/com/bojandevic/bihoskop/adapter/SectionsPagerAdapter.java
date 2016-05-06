package com.bojandevic.bihoskop.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bojandevic.bihoskop.MovieListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MovieListFragment mlf = new MovieListFragment();

        Bundle bundle = new Bundle();

        if (position == 0)
            bundle.putString(MovieListFragment.TABKEY, MovieListFragment.REPERTOAR);
        else if (position == 1)
            bundle.putString(MovieListFragment.TABKEY, MovieListFragment.USKORO);

        mlf.setArguments(bundle);

        return mlf;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Repertoar";
            case 1:
                return "Uskoro";
        }

        return null;
    }
}