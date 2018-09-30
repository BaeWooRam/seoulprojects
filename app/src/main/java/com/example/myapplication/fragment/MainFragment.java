package com.example.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class MainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    private LinearLayout mLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayout= (LinearLayout) inflater.inflate(R.layout.fragment_main,null);
        return mLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBottomNav = (BottomNavigationView) mLayout.findViewById(R.id.navigation);

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 2);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else
            selectedItem = mBottomNav.getMenu().getItem(2);

        selectedItem.setChecked(true);
        selectFragment(selectedItem);

        mBottomNav.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }


    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_home:
                frag = Container_HomeFragment.newInstance();

                break;
            case R.id.menu_News:
                frag = Container_NewsFragment.newInstance();

                break;
            case R.id.menu_mylocation:
                frag = Container_MyLocationFragment.newInstance();

                break;
            case R.id.menu_sponsor:
                frag= Container_SponsorFragment.newInstance();

                break;
            case R.id.menu_info:
                frag = Container_infoFragment.newInstance();

                break;

        }

        if (frag != null) {
            NavigateToFragment(frag,getFragmentManager(), R.id.main_container,false);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        selectFragment(menuItem);
        return true;
    }


    public static void NavigateToFragment(Fragment fragment, FragmentManager manager, int container_id, boolean addToBackstack) {
        FragmentTransaction transaction = manager.beginTransaction().replace(container_id, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}