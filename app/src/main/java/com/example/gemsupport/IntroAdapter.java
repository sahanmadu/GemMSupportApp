package com.example.gemsupport;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class IntroAdapter extends FragmentPagerAdapter {
    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new First();
            case 1: return new Second();
            case 2: return new Third();
            default: return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
