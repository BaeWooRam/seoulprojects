package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.Fragment.Fragment_NearHereInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigateToFragment(new Fragment_NearHereInfo(),getSupportFragmentManager(), R.id.main_container,false);
    }

    public static void NavigateToFragment(Fragment fragment, FragmentManager manager, int container_id, boolean addToBackstack) {
        FragmentTransaction transaction = manager.beginTransaction().replace(container_id, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
