package com.example.myapplication.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.home_viewpager_adapter;


public class Container_HomeFragment extends Fragment {


    public Container_HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static Container_HomeFragment newInstance() {
        Container_HomeFragment fragment = new Container_HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_container__home,container,false);

        ViewPager vp = (ViewPager)view.findViewById(R.id.homefragment_ad_viewpager);


        vp.setAdapter(new home_viewpager_adapter(getFragmentManager()));
        vp.setCurrentItem(0);


        return view;



    }

}
