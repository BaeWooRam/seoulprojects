package com.example.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.myapplication.fragment.in_the_viewpager_fragment.viewPager_1;
import com.example.myapplication.fragment.in_the_viewpager_fragment.viewPager_2;
import com.example.myapplication.fragment.in_the_viewpager_fragment.viewPager_3;
import com.example.myapplication.fragment.in_the_viewpager_fragment.viewPager_4;
import com.example.myapplication.fragment.in_the_viewpager_fragment.viewPager_5;

public class home_viewpager_adapter extends FragmentStatePagerAdapter {

    public home_viewpager_adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new viewPager_1();
            case 1:
                return new viewPager_2();
            case 2:
                return new viewPager_3();
            case 3:
                return new viewPager_4();
            case 4:
                return new viewPager_5();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }


}
